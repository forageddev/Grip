package dev.foraged.grip

import dev.foraged.grip.server.ServerService
import gg.scala.aware.Aware
import gg.scala.aware.AwareBuilder
import gg.scala.aware.codec.codecs.interpretation.AwareMessageCodec
import gg.scala.aware.message.AwareMessage
import gg.scala.store.serializer.serializers.GsonSerializer
import net.evilblock.cubed.serializers.Serializers

class GripShared
{
    companion object {
        @JvmStatic lateinit var instance: GripShared
    }

    lateinit var aware: Aware<AwareMessage>

    fun configure(vararg classes: Any) {
        instance = this

        aware = AwareBuilder
            .of<AwareMessage>("grip")
            .codec(AwareMessageCodec)
            .build()
        for (listen in classes) {
            aware.listen(listen)
        }
        aware.connect()

        GsonSerializer.provideCustomGson {
            Serializers.gson
        }
        ServerService.configure()
    }
}