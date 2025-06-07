package de.joker.kutils.paper.extensions

import net.minecraft.world.entity.player.Input

val Input.forwardInput: Float
    get() {
        return (if (forward) 1.0f else 0.0f) - (if (backward) 1.0f else 0.0f)
    }

val Input.sidewaysInput: Float
    get() {
        return (if (left) 1.0f else 0.0f) - (if (right) 1.0f else 0.0f)
    }

val Input.hasInput: Boolean
    get() {
        return forward || backward || left || right
    }