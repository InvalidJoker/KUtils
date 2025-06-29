package de.joker.kutils.paper.mineskin.models


data class MineSkinSingleSkinResponse(
    val success: Boolean,
    val skin: MineSkinResponse? = null,
    val warnings: List<MineSkinWarning>,
    val messages: List<Any>,
    val links: MineSkinLinks
)
