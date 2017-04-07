package com.mjr.extraplanets.planets.Pluto.worldgen;

import java.util.List;

import micdoodle8.mods.galacticraft.api.prefab.core.BlockMetaPair;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.ChunkProviderSpace;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.DungeonConfiguration;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.MapGenDungeon;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

import com.google.common.collect.Lists;
import com.mjr.extraplanets.blocks.ExtraPlanets_Blocks;
import com.mjr.extraplanets.blocks.planetAndMoonBlocks.BlockBasicPluto;

public class ChunkProviderPluto extends ChunkProviderSpace {
    //private final MapGenVillagePluto villageGenerator = new MapGenVillagePluto();

	private final BiomeDecoratorPluto plutoBiomeDecorator = new BiomeDecoratorPluto();

	private final MapGenCavePluto caveGenerator = new MapGenCavePluto();

	private final MapGenDungeon dungeonGenerator = new MapGenDungeonPluto(new DungeonConfiguration(ExtraPlanets_Blocks.PLUTO_BLOCKS.getDefaultState().withProperty(BlockBasicPluto.BASIC_TYPE, BlockBasicPluto.EnumBlockBasic.DUNGEON_BRICK), 30, 8, 16, 7, 7, RoomBossPluto.class, RoomTreasurePluto.class));

	public ChunkProviderPluto(World par1World, long seed, boolean mapFeaturesEnabled) {
		super(par1World, seed, mapFeaturesEnabled);
	}

	@Override
	protected BiomeDecoratorSpace getBiomeGenerator() {
		return this.plutoBiomeDecorator;
	}

	@Override
	protected Biome[] getBiomesForGeneration() {
		return new Biome[] { PlutoBiomes.pluto };
	}

	@Override
	protected int getSeaLevel() {
		return 93;
	}

	@Override
	protected List<MapGenBaseMeta> getWorldGenerators() {
		List<MapGenBaseMeta> generators = Lists.newArrayList();
		generators.add(this.caveGenerator);
		return generators;
	}

	@Override
	protected BlockMetaPair getGrassBlock() {
		return new BlockMetaPair(ExtraPlanets_Blocks.PLUTO_BLOCKS, (byte) 0);
	}

	@Override
	protected BlockMetaPair getDirtBlock() {
		return new BlockMetaPair(ExtraPlanets_Blocks.PLUTO_BLOCKS, (byte) 1);
	}

	@Override
	protected BlockMetaPair getStoneBlock() {
		return new BlockMetaPair(ExtraPlanets_Blocks.PLUTO_BLOCKS, (byte) 2);
	}

	@Override
	public double getHeightModifier() {
		return 12;
	}

	@Override
	public double getSmallFeatureHeightModifier() {
		return 26;
	}

	@Override
	public double getMountainHeightModifier() {
		return 95;
	}

	@Override
	public double getValleyHeightModifier() {
		return 50;
	}

	@Override
	public int getCraterProbability() {
		return 2000;
	}

	@Override
	public void onChunkProvide(int cX, int cZ, ChunkPrimer primer) {
		this.dungeonGenerator.generate(this.world, cX, cZ, primer);
	}

	@Override
	public void onPopulate(int cX, int cZ) {
		this.dungeonGenerator.generateStructure(this.world, this.rand, new ChunkPos(cX, cZ));
		//this.villageGenerator.generateStructure(this.world, this.rand, new ChunkCoordIntPair(cX, cZ));
	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
		this.dungeonGenerator.generate(this.world, x, z, null);
       //this.villageGenerator.generate(this, this.world, x, z, null);
	}
}
