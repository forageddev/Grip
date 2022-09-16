package dev.foraged.grip.server

import gg.scala.store.storage.storable.IDataStoreObject
import java.util.*

class Server(
    var name: String,
    var groups: List<String>,
    var tps: Double,
    var cpuUsage: Double,
    var players: Int,
    var maxPlayers: Int,
    var plugins: List<ServerPlugin>,
    var status: ServerStatus
) : IDataStoreObject
{
    override val identifier: UUID = UUID.nameUUIDFromBytes(name.toByteArray())
    var heartbeat: Long = System.currentTimeMillis()
    val metaData = mutableMapOf<String, String>()
    val booting: Boolean get() = status == ServerStatus.BOOTING
    val online: Boolean get() = status == ServerStatus.ONLINE || whitelisted
    val whitelisted: Boolean get() = status == ServerStatus.WHITELISTED
    val offline: Boolean get() = status == ServerStatus.OFFLINE

    val color: String get() {
        return when (status) {
            ServerStatus.BOOTING -> "§6"
            ServerStatus.WHITELISTED -> "§e"
            ServerStatus.ONLINE -> "§a"
            ServerStatus.OFFLINE -> "§c"
        }
    }

    fun hasActiveGroup(name: String) : Boolean {
        return name in groups
    }

    fun hasPlugin(name: String) : Boolean {
        return plugins.any {
            it.name.equals(name, true)
        }
    }

    operator fun get(key: String) : String? = metaData[key]
    operator fun set(key: String, value: String) {
        metaData[key] = value
    }
}
