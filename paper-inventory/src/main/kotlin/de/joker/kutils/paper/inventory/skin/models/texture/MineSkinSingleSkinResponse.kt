package de.joker.kutils.paper.inventory.skin.models.texture

import de.joker.kutils.paper.inventory.skin.models.MineSkinLinks
import de.joker.kutils.paper.inventory.skin.models.MineSkinWarning


data class MineSkinSingleSkinResponse(
    val success: Boolean,
    val skin: MineSkinResponse? = null,
    val warnings: List<MineSkinWarning>,
    val messages: List<Any>,
    val links: MineSkinLinks
)
