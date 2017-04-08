package com.mjr.extraplanets.planets.Ceres.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


public class BiomeGenCeres extends CeresBiomes {

	public BiomeGenCeres(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.CERES_BIOME_ID, CeresBiomes.ceres.getBiomeName(), CeresBiomes.ceres);
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
