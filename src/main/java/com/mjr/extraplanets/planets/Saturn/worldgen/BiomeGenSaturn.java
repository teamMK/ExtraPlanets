package com.mjr.extraplanets.planets.Saturn.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenSaturn extends SaturnBiomes {

	public BiomeGenSaturn(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.SATURN_BIOME_ID, SaturnBiomes.saturn.getBiomeName(), SaturnBiomes.saturn);
		BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
