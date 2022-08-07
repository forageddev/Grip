package dev.foraged.grip.command

import dev.foraged.commons.acf.CommandHelp
import dev.foraged.commons.acf.annotation.CommandAlias
import dev.foraged.commons.acf.annotation.CommandPermission
import dev.foraged.commons.acf.annotation.Default
import dev.foraged.commons.acf.annotation.HelpCommand
import dev.foraged.commons.acf.annotation.Subcommand
import dev.foraged.commons.annotations.commands.AutoRegister
import dev.foraged.commons.command.GoodCommand
import dev.foraged.grip.server.ServerService
import net.evilblock.cubed.util.CC
import net.evilblock.cubed.util.bukkit.Constants
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

@CommandAlias("grip")
@CommandPermission("grip.management")
@AutoRegister
object GripCommand : GoodCommand()
{
    @Default
    @HelpCommand
    fun help(page: CommandHelp) {
        page.showHelp()
    }

    @Subcommand("servers")
    fun execute(sender: Player) {
        ServerService.servers.forEach {
            sender.sendMessage("${it.color}${it.name} ${CC.GRAY}${Constants.DOUBLE_ARROW_RIGHT} (${it.players}/${it.players})")
        }
    }
}