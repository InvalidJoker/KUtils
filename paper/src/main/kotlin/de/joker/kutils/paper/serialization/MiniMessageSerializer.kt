package de.joker.kutils.paper.serialization

import dev.fruxz.stacked.extension.miniMessageSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import net.kyori.adventure.text.Component

object MiniMessageSerializer : KSerializer<Component> {
override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("MiniMessage", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Component = miniMessageSerializer.deserialize(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Component) = encoder.encodeString(miniMessageSerializer.serialize(value))
}