package dev.foraged.grip.example

import dev.foraged.commons.replacement.Replacement
import dev.foraged.commons.replacement.ReplacementBuilder
import dev.foraged.commons.replacement.annotation.ReplacementAutoRegister
import dev.foraged.grip.server.ServerService
import dev.foraged.grip.server.ServerStatus
import net.evilblock.cubed.util.CC

@ReplacementAutoRegister
object GripServerReplacement : Replacement {
    override fun id() = "grip"
    override val replacements =
        ReplacementBuilder
            .newBuilder()
            .processStatic("global") {
                ServerService.globalCount.toString()
            }
            .process("status") { _, id ->
                val server = ServerService.fetchServerByName(id).get() ?: return@process "${CC.RED}Server Offline"
                server.color + when (server.status) {
                    ServerStatus.BOOTING -> "Server Booting"
                    ServerStatus.ONLINE -> "Server Online"
                    ServerStatus.WHITELISTED -> "Server Whitelisted"
                    ServerStatus.OFFLINE -> "Server Offline"
                }
            }
            .process("players") { _, id ->
                (ServerService.fetchServerByName(id).get()?.players ?: 0).toString()
            }
            .process("max-players") { _, id ->
                (ServerService.fetchServerByName(id).get()?.maxPlayers ?: 0).toString()
            }
            .process("group-players") { _, id ->
                var count = 0
                ServerService.servers.forEach {
                    if (it.hasActiveGroup(id)) count += it.players
                }
                "$count"
            }
}