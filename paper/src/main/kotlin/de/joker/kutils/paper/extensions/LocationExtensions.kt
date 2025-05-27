package de.joker.kutils.paper.extensions

import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

operator fun Location.component1() = x
operator fun Location.component2() = y
operator fun Location.component3() = z
operator fun Location.component4() = yaw
operator fun Location.component5() = pitch

operator fun Block.component1() = x
operator fun Block.component2() = y
operator fun Block.component3() = z

infix fun Block.eqType(block: Block) = type == block.type
infix fun Block.eqType(material: Material) = type == material

fun Location.dropItem(item: ItemStack) = world.dropItem(this, item)
fun Location.dropItemNaturally(item: ItemStack) = world.dropItemNaturally(this, item)

fun Location.spawnArrow(direction: Vector, speed: Float, spread: Float) =
    world.spawnArrow(this, direction, speed, spread)

fun Location.generateTree(type: TreeType) = world.generateTree(this, type)

@Suppress("DEPRECATION")
fun Location.generateTree(type: TreeType, delegate: BlockChangeDelegate) = world.generateTree(this, type, delegate)

fun Location.strikeLightning() = world.strikeLightning(this)
fun Location.strikeLightningEffect() = world.strikeLightningEffect(this)

fun Location.playEffect(effect: Effect, data: Int) = world.playEffect(this, effect, data)
fun Location.playEffect(effect: Effect, data: Int, radius: Int) = world.playEffect(this, effect, data, radius)
fun <T> Location.playEffect(effect: Effect, data: T) = world.playEffect(this, effect, data)
fun <T> Location.playEffect(effect: Effect, data: T, radius: Int) = world.playEffect(this, effect, data, radius)

fun Location.playSound(sound: Sound, volume: Float, pitch: Float) = world.playSound(this, sound, volume, pitch)

fun Location.surroundings(): List<Location> {
    val list = mutableListOf<Location>()
    for (x in -1..1) {
        for (z in -1..1) {
            if (x == 0 && z == 0) continue
            list.add(this.clone().add(x.toDouble(), 0.0, z.toDouble()))
        }
    }
    return list
}

fun Location.asSafeLocation(): Location {
    // Now we test if the location (2 blocks high) is occupied and if so, we move up until we find a free spot
    var location = this.clone()
    while ((location.block.type.isSolid || location.clone().add(0.0, 1.0, 0.0).block.type.isSolid)) {
        location = location.add(0.0, 1.0, 0.0)
    }
    return location
}

fun Location.asSafeLocationOrNull(): Location? {
    // Now we test if the location (2 blocks high) is occupied and if so, we move up until we find a free spot
    var location = this.clone()
    while ((location.block.type.isSolid || location.clone().add(0.0, 1.0, 0.0).block.type.isSolid)) {
        location = location.add(0.0, 1.0, 0.0)
    }
    if(location.y > 319) return null
    return location
}

fun Location.isInRadius(middle: Location, radius: Int): Boolean {
    return this.x in middle.x - radius..middle.x + radius && this.z in middle.z - radius..middle.z + radius
}

fun Location.inWorld(world: World) = this.clone().apply { this.world = world }

fun Location.inWorld(worlds: String) = this.clone().apply { this.world = Bukkit.getWorld(worlds.lowercase()) }

fun Location.toNorth(): Location {
    return this.apply { this.yaw = 180.0f; this.pitch = 0f }
}

fun mainWorld() = Bukkit.getWorlds()[0]
fun chunk(world: World, x: Int, y: Int) = world.getChunkAt(x, y)
fun chunk(block: Block) = chunk(block.world, block.x shr 4, block.z shr 4)

inline fun <reified T : Entity> World.spawn(location: Location): T {
    return spawn(location, T::class.java)
}

inline fun <reified T : Entity> World.getEntitiesByClass(): Collection<T> {
    return getEntitiesByClass(T::class.java)
}

/**
 * Broadcast the given sound to all online players.
 *
 * @param sound The sound to be broadcasted.
 * @param volume The volume of the sound (default value is 1.0f).
 * @param pitch The pitch of the sound (default value is 1.0f).
 */
fun broadcastSound(sound: Sound, volume: Float = 1.0f, pitch: Float = 1.0f) {
    Bukkit.getOnlinePlayers().forEach { it.playSound(it, sound, volume, pitch) }
}


fun World.setSpawnLocation(block: Block): Boolean {
    return setSpawnLocation(block.x, block.y, block.z)
}


fun Location.matches(x: Int, y: Int, z: Int) = blockX == x && blockY == y && blockZ == z


/**
 * Calculates the value of alpha in radians based on the start location.
 *
 * @param start the starting location
 * @return the value of alpha in radians
 */
fun getAlphaInRadians(start: Location): Double {
    return (start.clone().yaw + 180) / 180 * Math.PI + 0 / 180 * Math.PI
}

/**
 * Calculates a new location with the specified world based on the player's eye location and the given alpha angle.
 *
 * @param playerEyeLocation the player's eye location
 * @param alpha the angle in radians
 * @return a new location with the specified world
 */
fun getLocationWithWorld(playerEyeLocation: Location, alpha: Double): Location {
    return Location(playerEyeLocation.clone().world, cos(alpha), 0.0, sin(alpha))
}
/**
 * Calculates the difference between two locations.
 *
 * @param location1 The first location.
 * @param location2 The second location.
 * @return The difference between the two locations.
 */
fun getDifferenceLocation(location1: Location, location2: Location): Location {
    return location1.clone().subtract(location2)
}


/**
 * Generates a list of edge points on the block's bounding box.
 *
 * @param location The location of the block.
 * @param stepSize The size of each step for generating points. Defaults to 0.1.
 * @return A list of edge points on the block's bounding box.
 */
fun generateBlockEdgePoints(location: Location, stepSize: Double = 0.1): List<Location> {
    val boundingBox = location.block.boundingBox

    val xCount = ((boundingBox.maxX - boundingBox.minX) / stepSize).toInt()
    val yCount = ((boundingBox.maxY - boundingBox.minY) / stepSize).toInt()
    val zCount = ((boundingBox.maxZ - boundingBox.minZ) / stepSize).toInt()

    val threshold = 0.15

    return mutableListOf<Location>().apply {
        for (xIndex in 0..xCount) {
            val x = boundingBox.minX + (xIndex * stepSize)
            for (yIndex in 0..yCount) {
                val y = boundingBox.minY + (yIndex * stepSize)
                for (zIndex in 0..zCount) {
                    val z = boundingBox.minZ + (zIndex * stepSize)

                    val isEdgeX = x <= boundingBox.minX + threshold || x >= boundingBox.maxX - threshold
                    val isEdgeY = y <= boundingBox.minY + threshold || y >= boundingBox.maxY - threshold
                    val isEdgeZ = z <= boundingBox.minZ + threshold || z >= boundingBox.maxZ - threshold

                    // Checking if the point is on the edge in exactly two dimensions.
                    val isEdge = listOf(isEdgeX, isEdgeY, isEdgeZ).count { it } == 2

                    if (isEdge) {
                        add(Location(location.world, x, y, z))
                    }
                }
            }
        }
    }
}


fun Location.getBoundingBoxBlockFaceMiddleLocation(blockFace: BlockFace): Location {
    val boundingBox = block.boundingBox
    val x =
        boundingBox.minX + ((boundingBox.maxX - boundingBox.minX) / 2) + (blockFace.modX * ((boundingBox.maxX - boundingBox.minX) / 2))
    val y =
        boundingBox.minY + ((boundingBox.maxY - boundingBox.minY) / 2) + (blockFace.modY * ((boundingBox.maxY - boundingBox.minY) / 2))
    val z =
        boundingBox.minZ + ((boundingBox.maxZ - boundingBox.minZ) / 2) + (blockFace.modZ * ((boundingBox.maxZ - boundingBox.minZ) / 2))

    return Location(world, x, y, z)
}
