package com.mjr.extraplanets.moons.Io.worldgen;

import com.mjr.extraplanets.Config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenIo extends IoBiomes {

	public BiomeGenIo(BiomeProperties properties) {
		super(properties);
		Biome.registerBiome(Config.IO_BIOME_ID, IoBiomes.io.getBiomeName(), IoBiomes.io);
		BiomeDictionary.addTypes(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
	}
}
