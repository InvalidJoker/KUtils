package de.joker.paper.inventory.skin

import kotlinx.serialization.Serializable

@Serializable
data class SKIN(
    val url: String
)

@Serializable
data class Textures(
    val SKIN: SKIN
)

@Serializable
data class MinecraftSkin(
    val textures: Textures
)