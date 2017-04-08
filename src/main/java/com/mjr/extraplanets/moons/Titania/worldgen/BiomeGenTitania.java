package com.mjr.extraplanets.moons.Titania.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenTitania extends TitaniaBiomes {

	public BiomeGenTitania(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.TITANIA_BIOME_ID, TitaniaBiomes.titania.getBiomeName(), TitaniaBiomes.titania);
		BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
