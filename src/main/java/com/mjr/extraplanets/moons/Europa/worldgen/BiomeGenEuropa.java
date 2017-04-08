package com.mjr.extraplanets.moons.Europa.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenEuropa extends EuropaBiomes {

	public BiomeGenEuropa(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.EUROPA_BIOME_ID, EuropaBiomes.europa.getBiomeName(), EuropaBiomes.europa);
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
