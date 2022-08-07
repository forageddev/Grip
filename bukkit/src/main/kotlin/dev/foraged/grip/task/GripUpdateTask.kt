package dev.foraged.grip.task

import dev.foraged.commons.annotations.runnables.Repeating
import dev.foraged.grip.GripExtendedPlugin
import dev.foraged.grip.server.ServerService
import dev.foraged.grip.server.ServerStatus
import me.lucko.spark.api.SparkProvider
import me.lucko.spark.api.statistic.StatisticWindow
import org.bukkit.Bukkit

@Repeating(20L)
class GripUpdateTask : Runnable
{
    override fun run() {
        val server = GripExtendedPlugin.instance.server
        server.heartbeat = System.currentTimeMillis()
        server.players = Bukkit.getServer().onlinePlayers.size
        server.maxPlayers = Bukkit.getServer().maxPlayers
        server.tps = SparkProvider.get().tps()!!.poll(StatisticWindow.TicksPerSecond.SECONDS_5)
        server.cpuUsage = SparkProvider.get().cpuProcess().poll(StatisticWindow.CpuUsage.MINUTES_1)
        if (server.status != ServerStatus.OFFLINE && server.status != ServerStatus.BOOTING) server.status = if (Bukkit.getServer().hasWhitelist()) ServerStatus.WHITELISTED else ServerStatus.ONLINE

        ServerService.saveServer(GripExtendedPlugin.instance.server)
        GripExtendedPlugin.instance.server = server
    }
}