package de.joker.kutils.paper.extensions

import de.joker.kutils.adventure.component
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Used to provide simple methods to set the Lore of an [ItemMeta]
 * @since 0.0.1
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @see setLore
 * @see addLore
 */
class LoreBuilder {
    val lore = mutableListOf<Component>()

    operator fun Component.unaryPlus() = lore.add(this)
    operator fun String.unaryPlus() = lore.add(this.component())
}

/**
 * Sets the Lore of the [ItemMeta]
 * @param builder the [LoreBuilder] to set
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
inline fun ItemMeta.setLore(builder: LoreBuilder.() -> Unit) =
    lore(LoreBuilder().apply(builder).lore)

/**
 * Appends the [LoreBuilder] to the current Lore of the [ItemMeta]
 * @param builder The [LoreBuilder] to append
 * @author Max Bossing
 * @since 0.0.1
 */
inline fun ItemMeta.addLore(builder: LoreBuilder.() -> Unit) =
    lore(lore()?: mutableListOf<Component>().apply { addAll(LoreBuilder().apply(builder).lore) })

/**
 * Creates an [ItemStack] and opens a builder for it
 * @param material the [Material] for this [ItemStack]
 * @param builder the builder for this [ItemStack]
 * @return The built [ItemStack]
 * @since 0.0.1
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 */
fun itemStack(material: Material, builder: (ItemStack.() -> Unit)? = null) =
    builder?.let { ItemStack(material).apply(it) } ?: ItemStack(material)

/**
 * Opens a Builder for this [ItemStack]s [ItemMeta]
 * @param T the type of [ItemMeta]
 * @param builder The builder
 * @return The [ItemStack] with the new [ItemMeta] applied
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
inline fun <reified T : ItemMeta> ItemStack.meta(builder: T.() -> Unit) =
    ((itemMeta as? T) ?: itemMeta(type, builder))?.let {
        itemMeta = it.apply(builder)
    }

/**
 * @see [meta]
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
@JvmName("simpleMeta")
inline fun ItemStack.meta(builder: ItemMeta.() -> Unit) = meta<ItemMeta>(builder)

/**
 * Creates a default [ItemMeta] for the given [Material] and opens a [builder] for it
 * @param T the type of [ItemMeta]
 * @param material the [Material] for which an [ItemMeta] should be created
 * @param builder the builder
 * @return the [ItemMeta] for [material] with [builder] applied
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
inline fun <reified T : ItemMeta> itemMeta(material: Material, builder: T.() -> Unit): T? =
    Bukkit.getItemFactory().getItemMeta(material).let {
        if (it is T) it.apply(builder) else null
    }

/**
 * Add [ItemFlag]s to this [ItemMeta]
 * @param itemFlags the [ItemFlag]s to add
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
fun ItemMeta.flag(vararg itemFlags: ItemFlag) = addItemFlags(*itemFlags)

/**
 * Remove [ItemFlag]s from this [ItemMeta]
 * @param itemFlags the [ItemFlag]s to remove
 * @author Jakob Kretzschmar (https://github.com/jakobkmar)
 * @since 0.0.1
 */
fun ItemMeta.removeFlag(vararg itemFlag: ItemFlag) = removeItemFlags(*itemFlag)

/**
 * Safe access to the [ItemStack]s DisplayName
 * @since 0.0.1
 * @author Jakob Kretzschmar (https://github.com/jakobkmar/kspigot)
 */
var ItemMeta.name: Component?
    get() = if (hasDisplayName()) displayName() else null
    set(value) = displayName(value ?: Component.empty())

/**
 * Safe access to the [ItemStack]s CustomModelData
 * @since 0.0.1
 * @author Jakob Kretzschmar (https://github.com/jakobkmar/kspigot)
 */
var ItemMeta.model: Int?
    get() = if (hasCustomModelData()) customModelData else null
    set(value) = setCustomModelData(value)