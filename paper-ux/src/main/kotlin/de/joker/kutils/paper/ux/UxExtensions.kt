package de.joker.kutils.paper.ux

import com.destroystokyo.paper.ParticleBuilder
import de.joker.kutils.paper.extensions.generateBlockEdgePoints
import de.joker.kutils.paper.extensions.getAlphaInRadians
import de.joker.kutils.paper.extensions.getBoundingBoxBlockFaceMiddleLocation
import de.joker.kutils.paper.extensions.getDifferenceLocation
import de.joker.kutils.paper.extensions.getLocationWithWorld
import de.joker.kutils.paper.extensions.minecraftTicks
import de.joker.kutils.paper.extensions.spawnColoredParticle
import de.joker.kutils.paper.ux.effect.ParticleData
import dev.fruxz.ascend.extension.time.inWholeMinecraftTicks
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


/**
 * Spawns a line of particles from the start location to the end location, with the given color.
 * The line is created by gradually moving the start location towards the end location.
 *
 * @param start The starting location of the particle line.
 * @param end The ending location of the particle line.
 * @param color The color of the particles to be spawned.
 */
fun spawnParticleLine(start: Location, end: Location, clickedFace: BlockFace, color: Color) {
    val blockLoc = end.getBoundingBoxBlockFaceMiddleLocation(clickedFace)
    val playerEyeLocation = start.clone().subtract(0.0, 0.45, 0.0)
    val alpha = getAlphaInRadians(start)

    val offset = getLocationWithWorld(playerEyeLocation, alpha)
    val newPlayerEyeLocation = playerEyeLocation.add(offset.clone().multiply(0.4))

    for (c in 10 downTo 1) {
        createParticle(start, blockLoc, color, newPlayerEyeLocation, c)
    }
}

/**
 * Creates a particle at the specified location with the given properties.
 *
 * @param start The starting location for the particle.
 * @param blockLoc The location of the block.
 * @param color The color of the particle.
 * @param playerEyeLocation The location of the player's eyes.
 * @param c The constant value used to calculate the particle's location.
 */
fun createParticle(start: Location, blockLoc: Location, color: Color, playerEyeLocation: Location, c: Int) {
    val t = (c / 10.0)
    val particleLocation = playerEyeLocation.clone().add(getDifferenceLocation(blockLoc, playerEyeLocation).multiply(t))
    start.world.spawnParticle(Particle.DUST, particleLocation, 1, 0.0, 0.0, 0.0, Particle.DustOptions(color, 1f))
}

/**
 * Generates particles in a rectangular space defined by two corner points.
 *
 * @param point1 the first corner point of the space
 * @param point2 the second corner point of the space
 * @param color the color of the particles
 */
fun generateParticlesAroundSpace(point1: Location, point2: Location, color: Color) {
    val points = mutableListOf<Location>()
    val y = point1.y

    val x1 = point1.blockX
    val x2 = point2.blockX

    val z1 = point1.blockZ
    val z2 = point2.blockZ

    val xMin = if (x1 < x2) x1 else x2
    val xMax = if (x1 > x2) x1 else x2

    val zMin = if (z1 < z2) z1 else z2
    val zMax = if (z1 > z2) z1 else z2

    for (x in xMin..xMax) {
        for (z in zMin..zMax) {
            points.add(Location(point1.world, x.toDouble(), y, z.toDouble()))
        }
    }

    points.forEach { it.spawnColoredParticle(color) }
}

/**
 * Spawns a block outline with the specified color at the given location.
 *
 * @param location The location where the block outline will be spawned.
 * @param color The color of the block outline.
 */
fun spawnBlockOutline(location: Location, color: Color) {
    val points = generateBlockEdgePoints(location)
    points.forEach { it.spawnColoredParticle(color) }
}

// Melody builder
fun buildMelody(builder: MelodyBuilder.() -> Unit): Melody {
    val melodyBuilder = MelodyBuilder()
    builder(melodyBuilder)
    return melodyBuilder.build()
}

// Melody and Beat classes
class Melody(
    private var ticksPerBeat: Long = 10,
    private var ticksPerSound: Long = 0,
    var repetitions: Int = 0,
    private val beats: List<Beat>
) {
    var delayPerBeat: Duration
        get() = ticksPerBeat.takeIf { it > 0 }?.minecraftTicks ?: Duration.ZERO
        set(value) {
            ticksPerBeat = value.inWholeMinecraftTicks
        }

    var delayPerSound: Duration
        get() = ticksPerSound.takeIf { it > 0 }?.minecraftTicks ?: Duration.ZERO
        set(value) {
            ticksPerSound = value.inWholeMinecraftTicks
        }

    @OptIn(DelicateCoroutinesApi::class)
    fun play(player: Player) = GlobalScope.launch {
        repeat(1 + repetitions) {
            beats.forEach {
                it.play(player)
                delay(delayPerSound)
            }
            delay(delayPerBeat)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun play(location: Location) = GlobalScope.launch {
        repeat(1 + repetitions) {
            beats.forEach {
                it.play(location)
                delay(delayPerSound)
            }
            delay(delayPerBeat)
        }
    }
}

class Beat(private val sounds: List<SoundEffect>) {
    fun play(player: Player) {
        sounds.forEach { it.play(player) }
    }

    fun play(location: Location) {
        sounds.forEach { it.play(location) }
    }
}

data class SoundEffect(
    val sound: String,
    val volume: Float = 0.4f,
    val pitch: Float = 1f
) {

    constructor(sound: Sound, volume: Float = 0.4f, pitch: Float = 1f) : this(Registry.SOUNDS.getKey(sound).toString(), volume, pitch)
    fun play(player: Player) {
        player.playSound(player.location, sound, volume, pitch)
    }

    fun play(location: Location) {
        location.world.playSound(location, sound, volume, pitch)
    }
}

class MelodyBuilder {
    private val beats = mutableListOf<Beat>()
    private var repetitions = 0
    private var delayPerBeat: Duration = Duration.ZERO
    private var delayPerSound: Duration = Duration.ZERO

    fun beat(vararg sounds: SoundEffect) {
        beats.add(Beat(sounds.toList()))
    }

    fun repeat(times: Int) {
        repetitions = times
    }

    fun delayPerBeat(duration: Duration) {
        delayPerBeat = duration
    }

    fun delayPerSound(duration: Duration) {
        delayPerSound = duration
    }

    fun build(): Melody = Melody(
        ticksPerBeat = delayPerBeat.inWholeMinecraftTicks,
        ticksPerSound = delayPerSound.inWholeMinecraftTicks,
        repetitions = repetitions,
        beats = beats.toList()
    )
}

// Utility function to create SoundEffect
fun soundOf(sound: Sound, pitch: Float = 1.0f, volume: Float = 1.0f): SoundEffect {
    return SoundEffect(sound, pitch, volume)
}

fun PotionEffect(type: PotionEffectType, durationTicks: Int, amplifier: Int = 0, ambient: Boolean = true, particles: Boolean = true, icon: Boolean = true) =
    PotionEffect(
        type,
        durationTicks,
        amplifier,
        ambient,
        particles,
        icon
    )

fun PotionEffect(type: PotionEffectType, duration: Duration = 10.seconds, amplifier: Int = 0, ambient: Boolean = true, particles: Boolean = true, icon: Boolean = true) =
    PotionEffect(type, duration.inWholeMinecraftTicks.toInt(), amplifier, ambient, particles, icon)

fun buildPotionEffect(type: PotionEffectType, duration: Duration = 10.seconds, amplifier: Int = 0, ambient: Boolean = true, particles: Boolean = true, icon: Boolean = true, builder: PotionEffect.() -> Unit) =
    PotionEffect(type, duration, amplifier, ambient, particles, icon).apply(builder)


@Throws(IllegalStateException::class)
fun ParticleBuilder.playParticleEffect(reach: Double = .0) {
    val location = location()
    val internalReceivers = receivers()?.toList() ?: location?.world?.players

    if (location != null) {
        internalReceivers!!

        if (reach > 0) {
            val participants = location.getNearbyPlayers(reach).filter { internalReceivers.contains(it) }
            val computedParticleBuilder = receivers(participants)

            computedParticleBuilder.spawn()

        } else
            spawn()

    } else
        throw IllegalStateException("'location'[bukkit.Location] of ParticleBuilder cannot be null!")
}

@Throws(IllegalStateException::class)
fun ParticleBuilder.playParticleEffect(reach: Number = .0) =
    playParticleEffect(reach.toDouble())

fun ParticleBuilder.playParticleEffect() =
    playParticleEffect(.0)

fun ParticleBuilder.offset(offset: Number) = offset(offset.toDouble(), offset.toDouble(), offset.toDouble())

fun ParticleBuilder.offset(offsetX: Number, offsetZ: Number) = offset(offsetX.toDouble(), .0, offsetZ.toDouble())

fun ParticleBuilder.loc(loc: Location) = location(loc)

fun particleOf(particle: Particle): ParticleData = ParticleData(particle)

fun buildParticle(particle: Particle, builder: ParticleData.() -> Unit) =
    ParticleData(particle).apply(builder)

fun ParticleBuilder.copy(
    particle: Particle = particle(),
    receivers: List<Player>? = receivers(),
    source: Player? = source(),
    location: Location? = location(),
    count: Int = count(),
    offsetX: Double = offsetX(),
    offsetY: Double = offsetY(),
    offsetZ: Double = offsetZ(),
    extra: Double = extra(),
    data: Any? = data(),
    force: Boolean = force(),
) = ParticleBuilder(particle)
    .receivers(receivers)
    .source(source)
    .count(count)
    .offset(offsetX, offsetY, offsetZ)
    .extra(extra)
    .data(data)
    .force(force)
    .let { if (location != null) it.location(location) else it }
