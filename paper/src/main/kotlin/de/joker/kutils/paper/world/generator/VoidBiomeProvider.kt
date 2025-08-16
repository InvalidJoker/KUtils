package de.joker.kutils.paper.world.generator

import org.bukkit.block.Biome
import org.bukkit.generator.BiomeParameterPoint
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.WorldInfo

class VoidBiomeProvider: BiomeProvider() {

    override fun getBiome(worldInfo: WorldInfo, x: Int, y: Int, z: Int): Biome {
        return Biome.THE_VOID
    }

    override fun getBiomes(worldInfo: WorldInfo): MutableList<Biome> {
        return mutableListOf(Biome.THE_VOID)
    }

    override fun getBiome(
        worldInfo: WorldInfo,
        x: Int,
        y: Int,
        z: Int,
        biomeParameterPoint: BiomeParameterPoint
    ): Biome {
        return Biome.THE_VOID
    }

}