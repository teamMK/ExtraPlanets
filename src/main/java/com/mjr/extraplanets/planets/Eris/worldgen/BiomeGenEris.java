package com.mjr.extraplanets.planets.Eris.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenEris extends ErisBiomes {

	public BiomeGenEris(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.ERIS_BIOME_ID, ErisBiomes.eris.getBiomeName(), ErisBiomes.eris);
		BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
