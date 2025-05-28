package de.joker.kutils.paper.extensions

import dev.fruxz.stacked.extension.Title
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import kotlin.math.roundToInt


fun Title.copy(
    title: Component = this.title(),
    subtitle: Component = this.subtitle(),
    times: Times? = this.times(),
) = Title(title, subtitle, times)

/**
 * Spawns particles at the specified location.
 *
 * @param particle the type of particle to spawn
 * @param count the number of particles to spawn
 * @param offsetX the x-axis offset of the particle
 * @param offsetY the y-axis offset of the particle
 * @param offsetZ the z-axis offset of the particle
 * @param speed the speed of the particles
 * @param dustOptions the dust options for the particles (if applicable)
 */
fun Location.spawnParticle(
    particle: Particle,
    count: Int,
    offsetX: Double,
    offsetY: Double,
    offsetZ: Double,
    speed: Double,
    dustOptions: Particle.DustOptions
) {
    this.world.spawnParticle(particle, this, count, offsetX, offsetY, offsetZ, speed, dustOptions)
}

/**
 * Spawns a colored particle at the given location.
 *
 * @param color The color of the particle. Defaults to white.
 */
fun Location.spawnColoredParticle(color: Color = Color.WHITE) {
    spawnParticle(Particle.DUST, 1, 0.0, 0.0, 0.0, 0.0, Particle.DustOptions(color, 0.4f))
}


/**
 * Generates a list of components with a color gradient between [startColor] and [endColor].
 * @param text The text to apply the color transition to.
 * @param steps The number of steps in the transition.
 * @param startColor The starting color.
 * @param endColor The ending color.
 * @return A list of colored Components for each step.
 */
fun interpolateColorText(
    text: String,
    steps: Int,
    startColor: TextColor,
    endColor: TextColor
): List<Component> {
    fun interpolate(start: Int, end: Int, fraction: Float): Int {
        return (start + (end - start) * fraction).roundToInt().coerceIn(0, 255)
    }

    return List(steps) { i ->
        val t = i / (steps - 1).toFloat()

        val r = interpolate(startColor.red(), endColor.red(), t)
        val g = interpolate(startColor.green(), endColor.green(), t)
        val b = interpolate(startColor.blue(), endColor.blue(), t)

        Component.text(text, TextColor.color(r, g, b))
    }
}