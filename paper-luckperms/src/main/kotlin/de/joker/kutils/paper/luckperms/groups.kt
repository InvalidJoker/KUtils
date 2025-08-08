package de.joker.kutils.paper.luckperms

import dev.fruxz.stacked.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.luckperms.api.model.group.Group
import net.luckperms.api.node.NodeType
import kotlin.collections.filter

val Group.absoluteWeight: Int
    get() = weight.orElse(0)

val Group.color: TextColor?
    get() {
        val rawColor = nodes.filter { NodeType.META.matches(it) }.map { NodeType.META.cast(it) }.firstOrNull { it.metaKey == "color" }?.metaValue ?: return null
        return TextColor.fromHexString("#$rawColor")
    }

val Group.prefix: String?
    get() = nodes.filter { NodeType.PREFIX.matches(it) }.map { NodeType.PREFIX.cast(it) }.maxByOrNull { it.priority }?.metaValue

val Group.prefixComponent: Component?
    get() = prefix?.let { text(it) }

val Group.suffix: String?
    get() = nodes.filter { NodeType.SUFFIX.matches(it) }.map { NodeType.SUFFIX.cast(it) }.maxByOrNull { it.priority }?.metaValue

val Group.suffixComponent: Component?
    get() = suffix?.let { text(it) }