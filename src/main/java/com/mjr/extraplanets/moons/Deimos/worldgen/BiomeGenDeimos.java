package com.mjr.extraplanets.moons.Deimos.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenDeimos extends DeimosBiomes {

	public BiomeGenDeimos(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.DEIMOS_BIOME_ID, DeimosBiomes.deimos.getBiomeName(), DeimosBiomes.deimos);
        BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
