package de.joker.kutils.paper.extensions

import org.bukkit.util.Vector

fun lerpVector(start: Vector, end: Vector, t: Float): Vector {
    val newX = start.getX() + t * (end.getX() - start.getX());
    val newY = start.getY() + t * (end.getY() - start.getY());
    val newZ = start.getZ() + t * (end.getZ() - start.getZ());
    return Vector(newX, newY, newZ);
}

fun lerp(currentValue: Float, targetValue: Float, deltaTime: Float, speed: Float): Float {
    val difference = targetValue - currentValue
    return currentValue + difference * speed * deltaTime
}