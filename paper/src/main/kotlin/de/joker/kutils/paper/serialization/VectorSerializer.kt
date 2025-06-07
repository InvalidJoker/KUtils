package de.joker.kutils.paper.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import org.bukkit.util.Vector

object VectorSerializer : KSerializer<Vector> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Vector") {
        element<Double>("x")
        element<Double>("y")
        element<Double>("z")
    }

    override fun serialize(encoder: Encoder, value: Vector) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeDoubleElement(descriptor, 0, value.x)
        composite.encodeDoubleElement(descriptor, 1, value.y)
        composite.encodeDoubleElement(descriptor, 2, value.z)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Vector {
        val dec = decoder.beginStructure(descriptor)
        var x = 0.0
        var y = 0.0
        var z = 0.0

        loop@ while (true) {
            when (val index = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> x = dec.decodeDoubleElement(descriptor, 0)
                1 -> y = dec.decodeDoubleElement(descriptor, 1)
                2 -> z = dec.decodeDoubleElement(descriptor, 2)
                else -> throw SerializationException("Unknown index $index")
            }
        }

        dec.endStructure(descriptor)
        return Vector(x, y, z)
    }
}