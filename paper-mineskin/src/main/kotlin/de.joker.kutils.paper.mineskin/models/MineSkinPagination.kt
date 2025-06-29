package de.joker.kutils.paper.mineskin.models

data class MineSkinPagination(
    val current: After? = null,
    val next: After? = null,
)

data class After(
    val after: String
)
