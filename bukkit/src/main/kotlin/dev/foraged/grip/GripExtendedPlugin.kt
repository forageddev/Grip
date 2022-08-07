package dev.foraged.grip

import dev.foraged.commons.ExtendedPaperPlugin
import dev.foraged.commons.annotations.container.ContainerEnable
import dev.foraged.commons.config.annotations.ContainerConfig
import dev.foraged.grip.configuration.GripConfiguration
import dev.foraged.grip.server.Server
import dev.foraged.grip.server.ServerPlugin
import dev.foraged.grip.server.ServerStatus
import me.lucko.helper.plugin.ap.Plugin
import me.lucko.helper.plugin.ap.PluginDependency
import me.lucko.spark.api.SparkProvider
import me.lucko.spark.api.statistic.StatisticWindow
import net.evilblock.cubed.util.bukkit.Tasks
import org.bukkit.Bukkit

@Plugin(
    name = "Grip",
    version = "\${git.commit.id.abbrev}",
    depends = [
        PluginDependency("Commons"),
        PluginDependency("spark")
    ]
)
@ContainerConfig(
    value = "settings",
    model = GripConfiguration::class
)
class GripExtendedPlugin : ExtendedPaperPlugin()
{
    companion object {
        lateinit var instance: GripExtendedPlugin
    }

    lateinit var server: Server

    @ContainerEnable
    fun containerEnable()
    {
        instance = this

        val plugins = mutableListOf<ServerPlugin>().also {
            Bukkit.getServer().pluginManager.plugins.forEach { plugin ->
                it.add(ServerPlugin(plugin.name, plugin.description.version ?: "None"))
            }
        }

        server = Server(
            Bukkit.getServerName(),
            config<GripConfiguration>().groups,
            SparkProvider.get().tps()!!.poll(StatisticWindow.TicksPerSecond.SECONDS_5),
            SparkProvider.get().cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_1),
            Bukkit.getServer().onlinePlayers.size,
            Bukkit.getServer().maxPlayers,
            plugins,
            ServerStatus.BOOTING
        )

        Tasks.asyncDelayed(40L) {
            server.status = if (Bukkit.getServer().hasWhitelist()) ServerStatus.WHITELISTED else ServerStatus.ONLINE
        }
        GripShared().configure()
    }
}
