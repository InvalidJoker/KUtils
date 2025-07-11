package de.joker.kutils.paper.inventory

import com.destroystokyo.paper.profile.ProfileProperty
import com.google.gson.Gson
import de.joker.kutils.paper.inventory.skin.MinecraftSkin
import de.joker.kutils.paper.inventory.skin.SKIN
import de.joker.kutils.paper.inventory.skin.Textures
import dev.fruxz.ascend.extension.forceCastOrNull
import dev.fruxz.stacked.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.world.item.component.CustomModelData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.craftbukkit.inventory.components.CraftCustomModelDataComponent
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*


/**
 * The ItemBuilder class is used for building ItemStack objects with various properties and customizations.
 * It provides methods for modifying the display name, lore, enchantments, flags, and other attributes of an ItemStack.
 *
 * @param material The material of the ItemStack.
 * @param count The count (stack size) of the ItemStack. Default is 1.
 * @param dsl The DSL (Domain Specific Language) block that can be used to customize the item. Default is an empty block.
 */
@Suppress("UnstableApiUsage", "Unused")
class ItemBuilder(material: Material, count: Int = 1, dsl: ItemBuilder.() -> Unit = {}) {

    /**
     * The itemStack to get.
     * This variable represents an ItemStack object that can be created using the
     * material and count parameters.
     */
    var itemStack = ItemStack(material, count)

    init {
        dsl.invoke(this)
    }

    /**
     * Change the displayname of the item.
     *
     * @param displayName The new displayname (could be rgb).
     * @return The updated ItemBuilder with the new displayname.
     */
    fun display(displayName: String): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.displayName(text(displayName).decoration(TextDecoration.ITALIC, false))
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Sets the amount of the item stack.
     *
     * @param amount The amount to set for the item stack.
     * @return The updated ItemBuilder instance.
     */
    fun amount(amount: Int): ItemBuilder {
        itemStack.amount = amount
        return this
    }

    /**
     * Updates the item meta by applying the provided DSL (Domain Specific Language) to it.
     *
     * @param dsl The DSL block to apply to the item meta.
     * @return The updated ItemBuilder instance.
     */
    fun <T : ItemMeta> meta(dsl: T.() -> Unit): ItemBuilder {
        val meta = itemStack.itemMeta.forceCastOrNull<T>() ?: return this
        dsl.invoke(meta)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Adds persistent data to the item.
     *
     * @param key The key used to identify the persistent data.
     * @param value The value of the persistent data.
     * @return The updated ItemBuilder object.
     */
    fun addPersistentData(key: NamespacedKey, value: String): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Adds persistent data to the item stack.
     *
     * @param key The namespaced key to associate with the data.
     * @param persistentDataType The type of the persistent data.
     * @param value The value of the persistent data.
     * @return The updated ItemBuilder instance.
     */
    fun <T : Any> addPersistentData(
        key: NamespacedKey,
        persistentDataType: PersistentDataType<T, T>,
        value: T
    ): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.persistentDataContainer.set(key, persistentDataType, value)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Adds persistent data to the item if the specified condition is true.
     *
     * @param key the namespaced key of the data
     * @param value the value of the data as a string
     * @param condition the condition that must be met to add the data (default: false)
     * @return the ItemBuilder instance
     */
    fun addPersistentDataIf(key: NamespacedKey, value: String, condition: Boolean = false): ItemBuilder {
        if (condition) {
            val meta = itemStack.itemMeta
            meta.persistentDataContainer.set(key, PersistentDataType.STRING, value)
            itemStack.itemMeta = meta
            return this
        }
        return this
    }

    /**
     * Removes persistent data from the item if the condition is true.
     *
     * @param key the NamespacedKey used to identify the persistent data
     * @param condition the condition to check before removing the data (defaults to false)
     * @return the ItemBuilder with the persistent data removed (or unchanged if the condition is false)
     */
    fun removePersistantDataIf(key: NamespacedKey, condition: Boolean = false): ItemBuilder {
        if (condition) {
            val meta = itemStack.itemMeta
            meta.persistentDataContainer.remove(key)
            itemStack.itemMeta = meta
            return this
        }
        return this
    }

    /**
     * Functional interface representing a performer that performs an operation on an item.
     *
     * @param T the type of the item to perform the operation on
     */
    @FunctionalInterface
    fun interface Performer<T> {
        fun perform(itemBuilder: T): T
    }

    /**
     * Checks the given condition and performs the specified operation on the ItemBuilder if the condition is true.
     *
     * @param condition the condition to be checked
     * @param consumer the operation to be performed on the ItemBuilder if the condition is true
     * @return the updated ItemBuilder if the condition is true, otherwise the original ItemBuilder
     */
    fun condition(condition: Boolean, consumer: Performer<ItemBuilder>): ItemBuilder {
        if (condition) {
            return consumer.perform(this)
        }
        return this
    }

    /**
     * Sets whether the item should have a glint effect or not.
     *
     * @param glinting true if the item should have a glint effect, false otherwise
     * @return the updated ItemBuilder instance
     */
    fun glinting(glinting: Boolean): ItemBuilder {
        meta<ItemMeta> {
            this.setEnchantmentGlintOverride(glinting)
        }
        return this
    }

    fun customModelDataComponents(
        floats: List<Float> = listOf(),
        flags: List<Boolean> = listOf(),
        strings: List<String> = listOf(),
        colors: List<Int> = listOf()
    ): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.setCustomModelDataComponent(CraftCustomModelDataComponent(CustomModelData(floats, flags, strings, colors)))
        itemStack.itemMeta = meta
        return this
    }

    fun addCustomString(value: String): ItemBuilder {
        val meta = itemStack.itemMeta
        val previous = meta.customModelDataComponent
        meta.setCustomModelDataComponent(CraftCustomModelDataComponent(CustomModelData(previous.floats, previous.flags, (previous.strings + value).distinct(), previous.colors.map { it.asARGB() })))
        itemStack.itemMeta = meta
        return this
    }

    fun getCustomStrings(): List<String> {
        val meta = itemStack.itemMeta
        val customModelDataComponent = meta.customModelDataComponent
        return customModelDataComponent.strings
    }

    /**
     * Sets the owner of the skull to the given name.
     *
     * @param name The name of the player who will own the skull
     * @return The updated ItemBuilder instance
     */
    fun owner(name: String): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val skullMeta = itemStack.itemMeta as SkullMeta
        skullMeta.owningPlayer = Bukkit.getOfflinePlayer(name)
        itemStack.itemMeta = skullMeta
        return this
    }

    /**
     * Sets the owner of the Player Head item.
     *
     * @param offlinePlayer the OfflinePlayer to set as the owner of the Player Head item
     * @return the modified ItemBuilder object
     */
    fun owner(offlinePlayer: OfflinePlayer): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val skullMeta = itemStack.itemMeta as SkullMeta
        skullMeta.owningPlayer = offlinePlayer
        itemStack.itemMeta = skullMeta
        return this
    }

    /**
     * Sets the owning player of the skull item.
     *
     * @param uuid The UUID of the player who should own the skull.
     * @return The modified ItemBuilder instance.
     */
    fun owner(uuid: UUID): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this
        val skullMeta = itemStack.itemMeta as SkullMeta
        val offlinePlayer = Bukkit.getOfflinePlayer(uuid)
        skullMeta.owningPlayer = offlinePlayer
        itemStack.itemMeta = skullMeta

        offlinePlayer.playerProfile.update().thenApply { profile ->
            skullMeta.playerProfile = profile
            itemStack.itemMeta = skullMeta
        }
        return this
    }

    /**
     * Sets the texture of the item to the specified texture.
     *
     * @param texture The name of the texture.
     * @return The ItemBuilder object with the texture set.
     */
    fun texture(texture: String, isUrl: Boolean = false): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this

        val skinUrl = if (isUrl) {
            texture
        } else {
            "https://textures.minecraft.net/texture/$texture"
        }

        val textureObject = MinecraftSkin(Textures(SKIN(skinUrl)))

        val gson = Gson()
        val base64 = Base64.getEncoder().encodeToString(gson.toJson(textureObject).toByteArray())

        return textureFromBase64(base64)
    }

    fun textureFromBase64(base64: String): ItemBuilder {
        if (itemStack.type != Material.PLAYER_HEAD) return this

        val uuid = UUID.randomUUID()
        val profile = Bukkit.getServer().createProfile(uuid, "head${uuid.toString().take(8)}")

        profile.setProperty(ProfileProperty("textures", base64))

        return meta<SkullMeta> {
            this.playerProfile = profile
        }
    }

    /**
     * Change the displayname of the item if a certain condition is true.
     *
     * @param displayName The new displayname (could be rgb).
     * @param condition Boolean value indicating if the displayname should be changed.
     * @return An instance of the ItemBuilder with the updated displayname, or the original instance if the condition is false.
     */
    fun displayIf(displayName: String, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return display(displayName)
        }
        return this
    }

    /**
     * Clears the display of the item if the given condition is true.
     *
     * @param condition true to clear the display, false otherwise
     * @return the updated ItemBuilder object
     */
    fun clearDisplayIf(condition: Boolean = false): ItemBuilder {
        if (condition) {
            return clearDisplay()
        }
        return this
    }

    /**
     * Clears the display name of the item.
     *
     * @return The updated ItemBuilder instance.
     */
    fun clearDisplay(): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.displayName(null)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Adds lore to an ItemBuilder object.
     *
     * @param lores A variable number of strings representing the lines of lore.
     * @return The updated ItemBuilder object with the lore added.
     */
    fun lore(vararg lores: String): ItemBuilder {
        val meta = itemStack.itemMeta
        val lore = mutableListOf<Component>()

        lores.forEach {
            val lines = it.split("\n")
            for (line in lines) {
                lore += text(line)
            }
        }

        meta.lore(lore.map { Component.text().decoration(TextDecoration.ITALIC, false).append(it).build() })
        itemStack.itemMeta = meta
        return this
    }

    // add a new one to the existing lore

    fun addLore(vararg lores: String): ItemBuilder {
        val meta = itemStack.itemMeta
        val lore = meta.lore()?.toMutableList() ?: mutableListOf()
        lores.forEach {
            val lines = it.split("\n")
            for (line in lines) {
                lore += text(line)
            }
        }
        meta.lore(lore.map { Component.text().decoration(TextDecoration.ITALIC, false).append(it).build() })
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Adds lore to the item builder if the given condition is true.
     *
     * @param lores The lore strings to add.
     * @param condition The condition that determines whether to add the lore or not. Default value is false.
     * @return The modified item builder.
     */
    fun loreIf(vararg lores: String, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return lore(*lores)
        }
        return this
    }

    /**
     * Clears the lore of the item.
     *
     * @return This ItemBuilder.
     */
    fun clearLore(): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.lore(null)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Clears the lore of the ItemBuilder if the given condition is true.
     *
     * @param condition the condition to check for clearing the lore
     * @return the modified ItemBuilder instance
     */
    fun clearLoreIf(condition: Boolean = false): ItemBuilder {
        if (condition) {
            return clearLore()
        }
        return this
    }

    /**
     * Add flags to the item.
     *
     * @param flags an array of ItemFlag objects representing the flags to be added
     * @return the ItemBuilder object with the added flags
     */
    fun flag(vararg flags: ItemFlag): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.addItemFlags(*flags)
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Clears all flags of the item.
     *
     * @return The updated ItemBuilder instance.
     */
    fun clearFlags(): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.removeItemFlags(*ItemFlag.entries.toTypedArray())
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Sets whether the item should have a glint effect or not.
     *
     * @param glinting true if the item should have a glint effect, false otherwise
     * @return the updated ItemBuilder instance
     */
    fun glinting(glinting: Boolean, force: Boolean = false): ItemBuilder {
        meta<ItemMeta> {
            if (!force) {
                this.setEnchantmentGlintOverride(if (glinting) true else null)
                return@meta
            }
            this.setEnchantmentGlintOverride(glinting)
        }
        return this
    }

    /**
     * Sets the rarity of the item.
     *
     * @param rarity the rarity to set
     * @return the ItemBuilder instance
     */
    fun rarity(rarity: ItemRarity): ItemBuilder {
        meta<ItemMeta> {
            this.setRarity(rarity)
        }
        return this
    }

    /**
     * Sets the equippable state of the item in the specified slot.
     */
    fun equippable(slot: EquipmentSlot): ItemBuilder {
        val meta = itemStack.itemMeta

        val equippable = meta.equippable

        equippable.slot = slot

        meta.setEquippable(equippable)

        itemStack.itemMeta = meta
        return this
    }

    /**
     * Clears the flags of the ItemBuilder object if the provided condition is true.
     *
     * @param condition the condition that determines whether to clear the flags
     * @return the ItemBuilder object with the flags cleared if the condition is true, otherwise returns the object as is
     */
    fun clearFlagsIf(condition: Boolean = false): ItemBuilder {
        if (condition) {
            return clearFlags()
        }
        return this
    }

    /**
     * Adds enchants to the item.
     *
     * @param enchants a map containing the enchantments to be added, where the key is the enchantment and the value is the level of the enchantment.
     * @return the modified ItemBuilder instance.
     */
    fun enchant(enchants: Map<Enchantment, Int>): ItemBuilder {
        val meta = itemStack.itemMeta
        enchants.forEach {
            meta.addEnchant(it.key, it.value, true)
        }
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Add enchants if condition is true
     *
     * @param enchants a map of enchantments to apply, where the key is the enchantment and the value is the level
     * @param condition a boolean value indicating whether to apply the enchantments or not. Default is false.
     * @return an instance of the ItemBuilder with the applied enchantments if condition is true, otherwise the same instance of ItemBuilder
     */
    fun enchantIf(enchants: Map<Enchantment, Int>, condition: Boolean = false): ItemBuilder {
        if (condition) {
            return enchant(enchants)
        }
        return this
    }

    /**
     * Clear all enchantments on the item.
     *
     * @return The ItemBuilder object with the enchantments cleared.
     */
    fun clearEnchants(): ItemBuilder {
        val meta = itemStack.itemMeta
        meta.enchants.forEach {
            meta.removeEnchant(it.key)
        }
        itemStack.itemMeta = meta
        return this
    }

    /**
     * Clears all enchantments if the specified condition is true.
     *
     * @param condition The condition that determines whether to clear enchantments.
     * @return An ItemBuilder object with cleared enchantments, or the original ItemBuilder object if the condition is false.
     */
    fun clearEnchantsIf(condition: Boolean = false): ItemBuilder {
        if (condition) {
            return clearEnchants()
        }
        return this
    }


    /**
     * Sets the material of the item stack.
     *
     * @param material the material to set
     * @return the ItemBuilder instance
     */
    fun type(material: Material): ItemBuilder {
        itemStack.type = material
        return this
    }

    /**
     * Sets the type of the material in the ItemBuilder, if the given condition is true.
     *
     * @param material The material to set.
     * @param condition The condition that determines whether the type should be set.
     * @return The ItemBuilder instance.
     */
    fun typeIf(material: Material, condition: Boolean = false): ItemBuilder {
        if (condition) {
            itemStack.type = material
        }
        return this
    }

    /**
     * Creates a deep copy of the current ItemBuilder object.
     *
     * @return a new ItemBuilder object that is a copy of the original.
     */
    fun clone(): ItemBuilder {
        val itemMeta = itemStack.itemMeta.clone()
        return ItemBuilder(itemStack.type, itemStack.amount) {
            itemStack = itemStack.clone().apply {
                this.itemMeta = itemMeta
            }
        }
    }

    fun durability(durability: Int): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is Damageable) {
            val maxDurability = itemStack.type.maxDurability
            if (durability < 0 || durability > maxDurability) {
                throw IllegalArgumentException("Durability must be between 0 and $maxDurability for ${itemStack.type}")
            }
            meta.damage = maxDurability - durability
            itemStack.itemMeta = meta
        } else {
            throw IllegalArgumentException("Item type ${itemStack.type} does not support durability")
        }
        return this
    }

    fun isExact(itemStack: ItemStack, withoutDurability: Boolean = true): Boolean {
        val itemToCheck = itemStack.clone()
        val thisItem = this.itemStack.clone()
        if (withoutDurability && itemStack.itemMeta is Damageable && this.itemStack.itemMeta is Damageable) {
            val damageableItemMeta = itemToCheck.itemMeta as Damageable
            val damageableThisItemMeta = thisItem.itemMeta as Damageable
            damageableItemMeta.damage = 0
            damageableThisItemMeta.damage = 0
            itemToCheck.itemMeta = damageableItemMeta
            thisItem.itemMeta = damageableThisItemMeta
        }

        return itemToCheck == thisItem
    }

    /**
     * Retrieves the built ItemStack from the builder.
     *
     * @return The built ItemStack object.
     */
    fun build(): ItemStack {
        return itemStack
    }

    /**
     * This class represents a companion object for the `ItemBuilder` class.
     * It provides utility methods for creating `ItemBuilder` instances from `ItemStack` objects.
     */
    companion object {
        val invalidMaterials = arrayListOf(
            Material.AIR,
            Material.CAVE_AIR,
            Material.VOID_AIR,
            Material.LAVA,
            Material.WATER,
        )

        fun fromItemStack(itemStack: ItemStack): ItemBuilder {
            val mat =
                if (invalidMaterials.contains(itemStack.type)) Material.GRASS_BLOCK else itemStack.type
            val builder = ItemBuilder(mat)
            builder.itemStack = itemStack
            return builder
        }
    }
}

/**
 * Converts a Material to an ItemBuilder and applies the provided DSL.
 *
 * @param dsl The DSL to be applied to the ItemBuilder.
 * @return The resulting ItemBuilder.
 */
fun Material.toItemBuilder(dsl: ItemBuilder.() -> Unit = {}): ItemBuilder {
    val mat = if (ItemBuilder.invalidMaterials.contains(this)) Material.GRASS_BLOCK else this
    return ItemBuilder(mat).apply(dsl)
}

/**
 * Converts an ItemStack to an ItemBuilder.
 *
 * @param dsl a lambda function that allows customization of the ItemBuilder.
 * @return the converted ItemBuilder.
 */
fun ItemStack.toItemBuilder(dsl: ItemBuilder.() -> Unit = {}): ItemBuilder {
    val mat = if (ItemBuilder.invalidMaterials.contains(this.type)) Material.GRASS_BLOCK else this.type
    val builder = ItemBuilder(mat)
    builder.itemStack = this
    builder.dsl()
    return builder
}
