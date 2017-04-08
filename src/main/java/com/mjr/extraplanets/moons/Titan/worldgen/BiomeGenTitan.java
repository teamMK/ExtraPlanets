package com.mjr.extraplanets.moons.Titan.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenTitan extends TitanBiomes {

	public BiomeGenTitan(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.TITAN_BIOME_ID, TitanBiomes.titan.getBiomeName(), TitanBiomes.titan);
		BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
