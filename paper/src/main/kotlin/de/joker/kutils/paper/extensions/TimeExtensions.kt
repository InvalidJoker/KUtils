package de.joker.kutils.paper.extensions

import kotlin.time.Duration

val Duration.inWholeMinecraftTicks: Int
    get() = inWholeMilliseconds.toInt() / 50