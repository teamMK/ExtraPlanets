package com.mjr.extraplanets.planets.Kepler22b.spacestation;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.IChunkProvider;

import com.mjr.extraplanets.planets.ExtraPlanets_Planets;
import com.mjr.extraplanets.planets.ExtraPlanets_SpaceStations;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldProviderKepler22bOrbit extends WorldProviderOrbit
{
	@Override
	public CelestialBody getCelestialBody()
	{
		return ExtraPlanets_SpaceStations.kepler22bSpaceStation;
	}

	@Override
	public Vector3 getFogColor()
	{
		return new Vector3(0, 0, 0);
	}

	@Override
	public Vector3 getSkyColor()
	{
		return new Vector3(0, 0, 0);
	}

	@Override
	public boolean canRainOrSnow()
	{
		return false;
	}

	@Override
	public boolean hasSunset()
	{
		return false;
	}

	@Override
	public long getDayLength()
	{
		return 24000L;
	}

	@Override
	public boolean shouldForceRespawn()
	{
		return true;
	}

	@Override
	public Class<? extends IChunkProvider> getChunkProviderClass()
	{
		return ChunkProviderKepler22bOrbit.class;
	}

	@Override
	public boolean isDaytime()
	{
		final float a = this.worldObj.getCelestialAngle(0F);
		//TODO: adjust this according to size of planet below
		return a < 0.42F || a > 0.58F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		final float var2 = this.worldObj.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

		if (var3 < 0.0F)
		{
			var3 = 0.0F;
		}

		if (var3 > 1.0F)
		{
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F + 0.3F;
	}


	@Override
	public boolean isSkyColored()
	{
		return false;
	}

	@Override
	public double getHorizon()
	{
		return 44.0D;
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 44;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}


	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2)
	{
		return true;
	}

	@Override
	public float getGravity()
	{
		return 0.073F;//0.073F;
	}

	@Override
	public boolean hasBreathableAtmosphere()
	{
		return false;
	}

	@Override
	public double getMeteorFrequency()
	{
		return 0;
	}

	@Override
	public double getFuelUsageMultiplier()
	{
		return 1.0D;
	}

	@Override
	public String getPlanetToOrbit()
	{
		return "planet.kepler22b";
	}

	@Override
	public int getYCoordToTeleportToPlanet()
	{
		return 10;
	}

	@Override
	public String getSaveFolder()
	{
		return "DIM_SPACESTATION_Kepler22b_" + this.spaceStationDimensionID;
	}

	@Override
	public double getSolarEnergyMultiplier()
	{
		return ConfigManagerCore.spaceStationEnergyScalar;
	}

	@Override
	public double getYCoordinateToTeleport()
	{
		return 1200;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier)
	{
		return tier > ExtraPlanets_Planets.kepler22b.getTierRequirement();
	}

	@Override
	public float getFallDamageModifier()
	{
		return 0.4F;
	}

	@Override
	public float getSoundVolReductionAmount()
	{
		return 50.0F;
	}
	
	@Override
	public float getThermalLevelModifier()
	{
		return 0;
	}

	@Override
	public float getWindLevel()
	{
		return 0.1F;
	}
}