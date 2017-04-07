package com.mjr.extraplanets.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.extraplanets.ExtraPlanets;
import com.mjr.extraplanets.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.extraplanets.handlers.capabilities.IStatsCapability;

public class ItemBasicItem extends Item {
	public ItemBasicItem(String assetName) {
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(assetName);
		this.setCreativeTab(ExtraPlanets.ItemsTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		if (player.world.isRemote) {
			if (itemStack.getItem() == ExtraPlanets_Items.TIER_1_ARMOR_LAYER)
				list.add(EnumColor.YELLOW + GCCoreUtil.translate("tier1ArmorLayer.desc"));
			else if (itemStack.getItem() == ExtraPlanets_Items.TIER_2_ARMOR_LAYER)
				list.add(EnumColor.YELLOW + GCCoreUtil.translate("tier2ArmorLayer.desc"));
			else if (itemStack.getItem() == ExtraPlanets_Items.TIER_3_ARMOR_LAYER)
				list.add(EnumColor.YELLOW + GCCoreUtil.translate("tier3ArmorLayer.desc"));
			else if (itemStack.getItem() == ExtraPlanets_Items.TIER_4_ARMOR_LAYER)
				list.add(EnumColor.YELLOW + GCCoreUtil.translate("tier4ArmorLayer.desc"));
			else if (itemStack.getItem() == ExtraPlanets_Items.ANTI_RADIATION)
				list.add(EnumColor.YELLOW + GCCoreUtil.translate("antiRadiation.desc"));
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		if (stack.getItem() == ExtraPlanets_Items.ANTI_RADIATION)
			return EnumAction.DRINK;
		return null;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		if (stack.getItem() == ExtraPlanets_Items.ANTI_RADIATION)
			return 32;
		return 0;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (stack.getItem() == ExtraPlanets_Items.ANTI_RADIATION) {
			EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;

			if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
				--stack.stackSize;
			}

			if (!worldIn.isRemote) {
				IStatsCapability stats = null;

				if (entityLiving != null) {
					stats = entityLiving.getCapability(CapabilityStatsHandler.EP_STATS_CAPABILITY, null);
				}
				stats.setRadiationLevel(stats.getRadiationLevel() / 2);
			}

			if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
				if (stack.stackSize <= 0) {
					return new ItemStack(Items.GLASS_BOTTLE);
				}

				if (entityplayer != null) {
					entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
				}
			}
		}
		return stack;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (stack.getItem() == ExtraPlanets_Items.ANTI_RADIATION)
			return 1;
		return this.maxStackSize;
	}
}