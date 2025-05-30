package de.joker.kutils.paper.ux.effect

import com.destroystokyo.paper.ParticleBuilder
import de.joker.kutils.core.math.CubicalShape
import dev.fruxz.ascend.extension.dump
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

data class ParticleData(
    val type: Particle,
) : ParticleBuilder(type), ParticleEffect {

    fun putData(data: Particle) =
        data(data)

    fun offset(cube: CubicalShape) =
        offset(cube.length, cube.height, cube.depth)

    fun edit(block: ParticleData.() -> Unit) = apply(block)

    override fun play(): Unit = spawn().dump()

    override fun play(vararg locations: Location?): Unit = locations.forEach { location ->
        if (location == null) return@forEach
        location(location).spawn()
    }

    override fun play(vararg entities: Entity?): Unit =
        receivers(entities.filterIsInstance<Player>()).spawn().dump()

    override fun play(locations: Set<Location>, entities: Set<Entity>) {
        val receivers = entities.filterIsInstance<Player>()

        locations.forEach { location ->
            location(location).receivers(receivers).spawn()
        }

    }

}