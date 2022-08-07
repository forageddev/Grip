package dev.foraged.grip.server

import gg.scala.store.controller.DataStoreObjectController
import gg.scala.store.controller.DataStoreObjectControllerCache
import gg.scala.store.storage.type.DataStoreStorageType
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ServerService {
    lateinit var serverController: DataStoreObjectController<Server>
    var globalCount: Int = 0

    fun configure() {
        serverController = DataStoreObjectControllerCache.create()

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            serverController.loadAll(DataStoreStorageType.REDIS).thenAccept {
                serverController.saveMultiple(DataStoreStorageType.CACHE, *it.values.toTypedArray())
            }

            var newGlobalCount = 0
            servers.forEach {
                newGlobalCount += it.players
            }
            globalCount = newGlobalCount
        }, 1L, 1L, TimeUnit.SECONDS)
    }

    val servers: Collection<Server> get() = serverController.loadAll(DataStoreStorageType.CACHE).join().values

    fun getServer(name: String) : Server? {
        return serverController.load(UUID.nameUUIDFromBytes(name.toByteArray()), DataStoreStorageType.CACHE).join()
    }

    fun fetchServerByName(name: String) : CompletableFuture<Server?> {
        return serverController.load(UUID.nameUUIDFromBytes(name.toByteArray()), DataStoreStorageType.REDIS)
    }

    fun saveServer(server: Server) {
        serverController.save(server, DataStoreStorageType.REDIS)
    }
}