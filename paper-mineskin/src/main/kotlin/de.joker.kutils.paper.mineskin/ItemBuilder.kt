package de.joker.kutils.paper.mineskin

import com.destroystokyo.paper.profile.ProfileProperty
import de.joker.kutils.paper.inventory.ItemBuilder
import de.joker.kutils.paper.mineskin.models.MineSkinResponse
import org.bukkit.Bukkit
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

/**
 * Retrieves the skin texture of a player from MineSkin API based on the provided MineSkin UUID.
 *
 * @param mineSkinUUID The MineSkin UUID of the player.
 * @return An ItemBuilder object representing the player's skin texture.
 */
fun ItemBuilder.textureFromMineSkin(mineSkinUUID: String): ItemBuilder {
    val fetcher = MineSkinFetcher.fetchSkinSignature(mineSkinUUID)

    if (fetcher == null) {
        throw IllegalArgumentException("Invalid MineSkin UUID: $mineSkinUUID")
    }

    return textureFromSkinTexture(fetcher)
}

/**
 * Generates an ItemBuilder with a custom texture based on the provided SkinTexture.
 *
 * @param skinTexture The SkinTexture object containing the UUID, name, and texture information.
 * @return An ItemBuilder instance with the custom texture applied.
 */
private fun ItemBuilder.textureFromSkinTexture(skinTexture: MineSkinResponse): ItemBuilder {
    val skinProfile = Bukkit.createProfile(UUID.randomUUID(), skinTexture.name)
    skinProfile.setProperty(
        ProfileProperty(
            "textures",
            skinTexture.texture.data.value,
            skinTexture.texture.data.signature
        )
    )
    val skullMeta = itemStack.itemMeta as SkullMeta
    skullMeta.playerProfile = skinProfile
    itemStack.itemMeta = skullMeta
    return this
}
