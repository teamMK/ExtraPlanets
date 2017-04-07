package com.mjr.extraplanets.handlers;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler.ThermalArmorEvent;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import com.google.common.collect.Lists;
import com.mjr.extraplanets.Config;
import com.mjr.extraplanets.ExtraPlanets;
import com.mjr.extraplanets.api.IPressureSuit;
import com.mjr.extraplanets.api.IRadiationSuit;
import com.mjr.extraplanets.client.handlers.capabilities.CapabilityProviderStatsClient;
import com.mjr.extraplanets.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.extraplanets.handlers.capabilities.CapabilityProviderStats;
import com.mjr.extraplanets.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.extraplanets.handlers.capabilities.IStatsCapability;
import com.mjr.extraplanets.items.ExtraPlanets_Items;
import com.mjr.extraplanets.network.ExtraPlanetsPacketHandler;
import com.mjr.extraplanets.network.PacketSimpleEP;
import com.mjr.extraplanets.network.PacketSimpleEP.EnumSimplePacket;
import com.mjr.extraplanets.util.DamageSourceEP;
import com.mjr.extraplanets.world.CustomWorldProviderSpace;

public class MainHandlerServer {

	private static List<ExtraPlanetsPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	public static void addPacketHandler(ExtraPlanetsPacketHandler handler) {
		MainHandlerServer.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (ExtraPlanetsPacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == Phase.END) {
			final WorldServer world = (WorldServer) event.world;

			for (ExtraPlanetsPacketHandler handler : packetHandlers) {
				handler.tick(world);
			}
		}
	}

	@SubscribeEvent
	public void onEntityDealth(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			final EntityLivingBase entityLiving = event.getEntityLiving();
			IStatsCapability stats = null;

			if (entityLiving != null) {
				stats = entityLiving.getCapability(CapabilityStatsHandler.EP_STATS_CAPABILITY, null);
			}
			stats.setRadiationLevel(0);
		}
	}

	@SubscribeEvent
	public void onPlayer(PlayerTickEvent event) {
		if (event.player.world.provider.getDimensionType().getId() == Config.JUPITER_ID) {
			Random rand = new Random();
			int addX = rand.nextInt(25);
			int addZ = rand.nextInt(25);
			if (rand.nextInt(2) == 1)
				addX = -addX;
			if (rand.nextInt(2) == 1)
				addZ = -addZ;
			int lightingSpawnChance = rand.nextInt(50);
			if (lightingSpawnChance == 10) {
				// event.player.world.addWeatherEffect(new EntityLightningBolt(event.player.world, event.player.posX + addX, event.player.world.getHeight(new BlockPos(event.player.posX + addX, 0, (int) event.player.posZ + addZ)).getY(),
				// event.player.posZ + addZ));
			}
		}
	}

	@SubscribeEvent
	public void onThermalArmorEvent(ThermalArmorEvent event) {
		if (event.armorStack == null) {
			event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.REMOVE);
			return;
		}
		if (event.armorStack.getItem() == AsteroidsItems.thermalPadding && event.armorStack.getItemDamage() == event.armorIndex) {
			event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
			return;
		}
		if (event.armorStack.getItem() == ExtraPlanets_Items.TIER_3_THERMAL_PADDING && event.armorStack.getItemDamage() == event.armorIndex) {
			event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
			return;
		}
		if (event.armorStack.getItem() == ExtraPlanets_Items.TIER_4_THERMAL_PADDING && event.armorStack.getItemDamage() == event.armorIndex) {
			event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
			return;
		}
		if (event.armorStack.getItem() == ExtraPlanets_Items.TIER_5_THERMAL_PADDING && event.armorStack.getItemDamage() == event.armorIndex) {
			event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.ADD);
			return;
		}
		event.setArmorAddResult(ThermalArmorEvent.ArmorAddResult.NOTHING);
	}

	@SubscribeEvent
	public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayerMP) {
			event.addCapability(CapabilityStatsHandler.EP_PLAYER_PROP, new CapabilityProviderStats((EntityPlayerMP) event.getObject()));
		} else if (event.getObject() instanceof EntityPlayerSP) {
			event.addCapability(CapabilityStatsClientHandler.EP_PLAYER_CLIENT_PROP, new CapabilityProviderStatsClient((EntityPlayerSP) event.getObject()));
		}
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		final EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving instanceof EntityPlayerMP) {
			onPlayerUpdate((EntityPlayerMP) entityLiving);
			if (OxygenUtil.isAABBInBreathableAirBlock(entityLiving.world, entityLiving.getEntityBoundingBox(), true) == false)
				runChecks(event, entityLiving);
		}
	}

	private void runChecks(LivingEvent.LivingUpdateEvent event, EntityLivingBase entityLiving) {
		if (((EntityPlayerMP) entityLiving).capabilities.isCreativeMode)
			return;
		if ((entityLiving.world.provider instanceof IGalacticraftWorldProvider) && (((EntityPlayerMP) entityLiving).world.provider instanceof CustomWorldProviderSpace)) {
			if (Config.PRESSURE)
				checkPressure(event, entityLiving);
			if (Config.RADIATION)
				checkRadiation(event, entityLiving);
		} else
			return;
	}

	private void checkPressure(LivingEvent.LivingUpdateEvent event, EntityLivingBase entityLiving) {
		EntityPlayerMP playerMP = (EntityPlayerMP) entityLiving;

		ItemStack helmet = playerMP.inventory.armorInventory[0];
		ItemStack chest = playerMP.inventory.armorInventory[1];
		ItemStack leggins = playerMP.inventory.armorInventory[2];
		ItemStack boots = playerMP.inventory.armorInventory[3];

		boolean doDamage = false;

		if (helmet == null || !(helmet.getItem() instanceof IPressureSuit))
			doDamage = true;
		else if (chest == null || !(chest.getItem() instanceof IPressureSuit))
			doDamage = true;
		else if (leggins == null || !(leggins.getItem() instanceof IPressureSuit))
			doDamage = true;
		else if (boots == null || !(boots.getItem() instanceof IPressureSuit))
			doDamage = true;

		if (doDamage) {
			float tempLevel = ((CustomWorldProviderSpace) playerMP.world.provider).getPressureLevel();
			tempLevel = (tempLevel / 100) * 8;
			playerMP.attackEntityFrom(DamageSourceEP.pressure, tempLevel);
		}
	}

	private void checkRadiation(LivingEvent.LivingUpdateEvent event, EntityLivingBase entityLiving) {
		EntityPlayerMP playerMP = (EntityPlayerMP) entityLiving;

		CustomWorldProviderSpace provider = (CustomWorldProviderSpace) playerMP.world.provider;
		// Normal/Nothing 0.005
		// Tier 1 0.0045
		// Tier 2 0.004
		// Tier 3 0.0035
		// Tier 4 0.00325
		boolean doDamage = false;
		boolean doArmorCheck = false;
		double damageModifer = 0;
		int radiationLevel = provider.getSolarRadiationLevel();
		if (playerMP.inventory.armorInventory[0] == null || playerMP.inventory.armorInventory[1] == null || playerMP.inventory.armorInventory[2] == null || playerMP.inventory.armorInventory[3] == null) {
			damageModifer = 0.2;
			doDamage = true;
		} else if (!(playerMP.inventory.armorInventory[0].getItem() instanceof IRadiationSuit) && !(playerMP.inventory.armorInventory[1].getItem() instanceof IRadiationSuit)
				&& !(playerMP.inventory.armorInventory[2].getItem() instanceof IRadiationSuit) && !(playerMP.inventory.armorInventory[3].getItem() instanceof IRadiationSuit)) {
			damageModifer = 0.2;
			doDamage = true;
		} else if (playerMP.inventory.armorInventory[0].getItem() instanceof IRadiationSuit) {
			doArmorCheck = true;
			doDamage = false;
		}
		if (doArmorCheck) {
			int helmetTier = ((IRadiationSuit) playerMP.inventory.armorInventory[0].getItem()).getArmorTier();
			int chestTier = ((IRadiationSuit) playerMP.inventory.armorInventory[1].getItem()).getArmorTier();
			int legginsTier = ((IRadiationSuit) playerMP.inventory.armorInventory[2].getItem()).getArmorTier();
			int bootsTier = ((IRadiationSuit) playerMP.inventory.armorInventory[3].getItem()).getArmorTier();

			int tierValue = (helmetTier + chestTier + legginsTier + bootsTier) / 2;
			double damageToTake = 0.005 * tierValue;
			damageModifer = 0.005 - (damageToTake / 2) / 10;
			doDamage = true;
		}
		if (doDamage) {
			IStatsCapability stats = null;
			if (playerMP != null) {
				stats = playerMP.getCapability(CapabilityStatsHandler.EP_STATS_CAPABILITY, null);
			}
			if (stats.getRadiationLevel() >= 100)
				playerMP.attackEntityFrom(DamageSourceEP.radiation, 3F);
			else if (stats.getRadiationLevel() >= 0)
				stats.setRadiationLevel(stats.getRadiationLevel() + (damageModifer * (radiationLevel / 10) / 6));
			else
				stats.setRadiationLevel(0);
		}
	}

	public void onPlayerUpdate(EntityPlayerMP player) {
		int tick = player.ticksExisted - 1;
		final boolean isInGCDimension = player.world.provider instanceof IGalacticraftWorldProvider;
		IStatsCapability stats = player.getCapability(CapabilityStatsHandler.EP_STATS_CAPABILITY, null);

		if (isInGCDimension && Config.RADIATION) {
			if (tick % 30 == 0) {
				this.sendSolarRadiationPacket(player, stats);
			}
		}
	}

	protected void sendSolarRadiationPacket(EntityPlayerMP player, IStatsCapability stats) {
		ExtraPlanets.packetPipeline.sendTo(new PacketSimpleEP(EnumSimplePacket.C_UPDATE_SOLAR_RADIATION_LEVEL, player.world.provider.getDimensionType().getId(), new Object[] { stats.getRadiationLevel() }), player);
	}

}
