package com.mjr.extraplanets.client.gui;

import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.extraplanets.client.gui.machines.GuiAdvancedOxygenCompressor;
import com.mjr.extraplanets.client.gui.machines.GuiAdvancedOxygenDecompressor;
import com.mjr.extraplanets.client.gui.machines.GuiAdvancedRefinery;
import com.mjr.extraplanets.client.gui.machines.GuiBasicChemicalInjector;
import com.mjr.extraplanets.client.gui.machines.GuiBasicCrystallizer;
import com.mjr.extraplanets.client.gui.machines.GuiBasicDecrystallizer;
import com.mjr.extraplanets.client.gui.machines.GuiBasicSmasher;
import com.mjr.extraplanets.client.gui.machines.GuiBasicSolarEvaporationChamber;
import com.mjr.extraplanets.client.gui.machines.GuiSolar;
import com.mjr.extraplanets.client.gui.machines.GuiUltimateOxygenCompressor;
import com.mjr.extraplanets.client.gui.machines.GuiUltimateOxygenDecompressor;
import com.mjr.extraplanets.client.gui.machines.GuiUltimateRefinery;
import com.mjr.extraplanets.inventory.machines.ContainerAdvancedOxygenCompressor;
import com.mjr.extraplanets.inventory.machines.ContainerAdvancedOxygenDecompressor;
import com.mjr.extraplanets.inventory.machines.ContainerAdvancedRefinery;
import com.mjr.extraplanets.inventory.machines.ContainerBasicChemicalInjector;
import com.mjr.extraplanets.inventory.machines.ContainerBasicCrystallizer;
import com.mjr.extraplanets.inventory.machines.ContainerBasicDecrystallizer;
import com.mjr.extraplanets.inventory.machines.ContainerBasicSmasher;
import com.mjr.extraplanets.inventory.machines.ContainerBasicSolarEvaporationChamber;
import com.mjr.extraplanets.inventory.machines.ContainerSolar;
import com.mjr.extraplanets.inventory.machines.ContainerUltimateOxygenCompressor;
import com.mjr.extraplanets.inventory.machines.ContainerUltimateOxygenDecompressor;
import com.mjr.extraplanets.inventory.machines.ContainerUltimateRefinery;
import com.mjr.extraplanets.tile.machines.TileEntityAdvancedOxygenCompressor;
import com.mjr.extraplanets.tile.machines.TileEntityAdvancedOxygenDecompressor;
import com.mjr.extraplanets.tile.machines.TileEntityAdvancedRefinery;
import com.mjr.extraplanets.tile.machines.TileEntityBasicChemicalInjector;
import com.mjr.extraplanets.tile.machines.TileEntityBasicCrystallizer;
import com.mjr.extraplanets.tile.machines.TileEntityBasicDecrystallizer;
import com.mjr.extraplanets.tile.machines.TileEntityBasicSmasher;
import com.mjr.extraplanets.tile.machines.TileEntityBasicSolarEvaporationChamber;
import com.mjr.extraplanets.tile.machines.TileEntitySolar;
import com.mjr.extraplanets.tile.machines.TileEntityUltimateOxygenCompressor;
import com.mjr.extraplanets.tile.machines.TileEntityUltimateOxygenDecompressor;
import com.mjr.extraplanets.tile.machines.TileEntityUltimateRefinery;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (playerBase == null) {
			player.sendMessage(new TextComponentString("ExtraPlanets player instance null server-side. This is a bug."));
			return null;
		}

		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null) {
			if (tile instanceof TileEntityAdvancedRefinery) {
				return new ContainerAdvancedRefinery(player.inventory, (TileEntityAdvancedRefinery) tile, player);
			} else if (tile instanceof TileEntityUltimateRefinery) {
				return new ContainerUltimateRefinery(player.inventory, (TileEntityUltimateRefinery) tile, player);
			} else if (tile instanceof TileEntitySolar) {
				return new ContainerSolar(player.inventory, (TileEntitySolar) tile);
			} else if (tile instanceof TileEntityAdvancedOxygenCompressor) {
				return new ContainerAdvancedOxygenCompressor(player.inventory, (TileEntityAdvancedOxygenCompressor) tile, player);
			} else if (tile instanceof TileEntityAdvancedOxygenDecompressor) {
				return new ContainerAdvancedOxygenDecompressor(player.inventory, (TileEntityAdvancedOxygenDecompressor) tile, player);
			} else if (tile instanceof TileEntityUltimateOxygenCompressor) {
				return new ContainerUltimateOxygenCompressor(player.inventory, (TileEntityUltimateOxygenCompressor) tile, player);
			} else if (tile instanceof TileEntityUltimateOxygenDecompressor) {
				return new ContainerUltimateOxygenDecompressor(player.inventory, (TileEntityUltimateOxygenDecompressor) tile, player);
			} else if (tile instanceof TileEntityBasicDecrystallizer) {
				return new ContainerBasicDecrystallizer(player.inventory, (TileEntityBasicDecrystallizer) tile, player);
			} else if (tile instanceof TileEntityBasicCrystallizer) {
				return new ContainerBasicCrystallizer(player.inventory, (TileEntityBasicCrystallizer) tile, player);
			} else if (tile instanceof TileEntityBasicSmasher) {
				return new ContainerBasicSmasher(player.inventory, (TileEntityBasicSmasher) tile, player);
			} else if (tile instanceof TileEntityBasicChemicalInjector) {
				return new ContainerBasicChemicalInjector(player.inventory, (TileEntityBasicChemicalInjector) tile, player);
			} else if (tile instanceof TileEntityBasicSolarEvaporationChamber) {
				return new ContainerBasicSolarEvaporationChamber(player.inventory, (TileEntityBasicSolarEvaporationChamber) tile, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return this.getClientGuiElement(ID, player, world, new BlockPos(x, y, z));
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos position) {
		TileEntity tile = world.getTileEntity(position);

		if (tile != null) {
			if (tile instanceof TileEntityAdvancedRefinery) {
				return new GuiAdvancedRefinery(player.inventory, (TileEntityAdvancedRefinery) world.getTileEntity(position));
			} else if (tile instanceof TileEntityUltimateRefinery) {
				return new GuiUltimateRefinery(player.inventory, (TileEntityUltimateRefinery) world.getTileEntity(position));
			} else if (tile instanceof TileEntitySolar) {
				return new GuiSolar(player.inventory, (TileEntitySolar) tile);
			} else if (tile instanceof TileEntityAdvancedOxygenCompressor) {
				return new GuiAdvancedOxygenCompressor(player.inventory, (TileEntityAdvancedOxygenCompressor) tile);
			} else if (tile instanceof TileEntityAdvancedOxygenDecompressor) {
				return new GuiAdvancedOxygenDecompressor(player.inventory, (TileEntityAdvancedOxygenDecompressor) tile);
			} else if (tile instanceof TileEntityUltimateOxygenCompressor) {
				return new GuiUltimateOxygenCompressor(player.inventory, (TileEntityUltimateOxygenCompressor) tile);
			} else if (tile instanceof TileEntityUltimateOxygenDecompressor) {
				return new GuiUltimateOxygenDecompressor(player.inventory, (TileEntityUltimateOxygenDecompressor) tile);
			} else if (tile instanceof TileEntityBasicDecrystallizer) {
				return new GuiBasicDecrystallizer(player.inventory, (TileEntityBasicDecrystallizer) tile);
			} else if (tile instanceof TileEntityBasicCrystallizer) {
				return new GuiBasicCrystallizer(player.inventory, (TileEntityBasicCrystallizer) tile);
			} else if (tile instanceof TileEntityBasicSmasher) {
				return new GuiBasicSmasher(player.inventory, (TileEntityBasicSmasher) tile);
			} else if (tile instanceof TileEntityBasicChemicalInjector) {
				return new GuiBasicChemicalInjector(player.inventory, (TileEntityBasicChemicalInjector) tile);
			} else if (tile instanceof TileEntityBasicSolarEvaporationChamber) {
				return new GuiBasicSolarEvaporationChamber(player.inventory, (TileEntityBasicSolarEvaporationChamber) tile);
			}
		}
		return null;
	}
}