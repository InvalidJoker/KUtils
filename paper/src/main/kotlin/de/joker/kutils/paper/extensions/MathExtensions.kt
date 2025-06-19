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

fun vec(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) = Vector(x.toDouble(), y.toDouble(), z.toDouble())
fun vecXY(x: Number, y: Number) = vec(x, y)
fun vecXZ(x: Number, z: Number) = vec(x, z = z)
fun vecYZ(y: Number, z: Number) = vec(y = y, z = z)
fun vecX(x: Number) = vec(x)
fun vecY(y: Number) = vec(y = y)
fun vecZ(z: Number) = vec(z = z)