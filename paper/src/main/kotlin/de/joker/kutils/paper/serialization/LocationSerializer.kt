package de.joker.kutils.paper.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationSerializer : KSerializer<Location> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Location") {
        element<String>("world")
        element<Double>("x")
        element<Double>("y")
        element<Double>("z")
        element<Float>("yaw")
        element<Float>("pitch")
    }

    override fun serialize(encoder: Encoder, value: Location) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.world?.name ?: throw SerializationException("Location has no world"))
        composite.encodeDoubleElement(descriptor, 1, value.x)
        composite.encodeDoubleElement(descriptor, 2, value.y)
        composite.encodeDoubleElement(descriptor, 3, value.z)
        composite.encodeFloatElement(descriptor, 4, value.yaw)
        composite.encodeFloatElement(descriptor, 5, value.pitch)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Location {
        val dec = decoder.beginStructure(descriptor)
        var worldName: String? = null
        var x = 0.0
        var y = 0.0
        var z = 0.0
        var yaw = 0f
        var pitch = 0f

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> worldName = dec.decodeStringElement(descriptor, 0)
                1 -> x = dec.decodeDoubleElement(descriptor, 1)
                2 -> y = dec.decodeDoubleElement(descriptor, 2)
                3 -> z = dec.decodeDoubleElement(descriptor, 3)
                4 -> yaw = dec.decodeFloatElement(descriptor, 4)
                5 -> pitch = dec.decodeFloatElement(descriptor, 5)
                else -> throw SerializationException("Unknown index $index")
            }
        }

        dec.endStructure(descriptor)

        val world = worldName?.let { Bukkit.getWorld(it) }
            ?: throw SerializationException("Unknown or unloaded world: $worldName")

        return Location(world, x, y, z, yaw, pitch)
    }
}
