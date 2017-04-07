package com.mjr.extraplanets.tile.machines;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

import com.mjr.extraplanets.blocks.ExtraPlanets_Blocks;
import com.mjr.extraplanets.blocks.machines.AdvancedRefinery;
import com.mjr.extraplanets.items.ExtraPlanets_Items;

public class TileEntityBasicSmasher extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = 50;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[3];

	private ItemStack producingStack = new ItemStack(ExtraPlanets_Items.POTASH_SHARDS, 3, 0);

	public TileEntityBasicSmasher() {
	}

	@Override
	public void update() {
		super.update();

		if (!this.world.isRemote) {
			if (this.canProcess() && canOutput() && this.hasEnoughEnergyToRun) {
				if (this.processTicks == 0) {
					this.processTicks = TileEntityBasicSmasher.PROCESS_TIME_REQUIRED;
				} else {
					if (--this.processTicks <= 0) {
						this.smeltItem();
						this.processTicks = this.canProcess() ? TileEntityBasicSmasher.PROCESS_TIME_REQUIRED : 0;
					}
				}
			} else {
				this.processTicks = 0;
			}
		}
	}

	public boolean canProcess() {
		if (this.containingItems[1] == null)
			return false;
		if (this.containingItems[1].getItem() != Item.getItemFromBlock(ExtraPlanets_Blocks.ORE_POTASH))
			return false;
		return !this.getDisabled(0);
	}

	public boolean canOutput() {
		ItemStack itemstack = this.producingStack;
		if (itemstack == null) {
			return false;
		}
		if (this.containingItems[2] == null) {
			return true;
		}
		if (!this.containingItems[2].isItemEqual(itemstack)) {
			return false;
		}
		int result = this.containingItems[2].stackSize + itemstack.stackSize;
		return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
	}

	public void smeltItem() {
		ItemStack resultItemStack = this.producingStack;
		if (this.canProcess() && canOutput()) {
			if (this.containingItems[2] == null) {
				this.containingItems[2] = resultItemStack.copy();
			} else if (this.containingItems[2].isItemEqual(resultItemStack)) {
				if (this.containingItems[2].stackSize + resultItemStack.stackSize > 64) {
					for (int i = 0; i < this.containingItems[2].stackSize + resultItemStack.stackSize - 64; i++) {
						float var = 0.7F;
						double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));
						entityitem.setPickupDelay(10);
						this.world.spawnEntity(entityitem);
					}
					this.containingItems[2].stackSize = 64;
				} else {
					this.containingItems[2].stackSize += resultItemStack.stackSize;
				}
			}
		}
		this.decrStackSize(1, 1);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.containingItems = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt);
		return nbt;
	}

	@Override
	protected ItemStack[] getContainingItems() {
		return this.containingItems;
	}

	@Override
	public String getName() {
		return GCCoreUtil.translate("container.basic.smasher.name");
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if (itemstack != null && this.isItemValidForSlot(slotID, itemstack)) {
			switch (slotID) {
			case 0:
				return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
			case 1:
				return itemstack.getItem() == Item.getItemFromBlock(ExtraPlanets_Blocks.ORE_POTASH);
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if (itemstack != null && this.isItemValidForSlot(slotID, itemstack)) {
			switch (slotID) {
			case 0:
				return itemstack.getItem() instanceof ItemElectricBase && ((ItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || !this.shouldPullEnergy();
			case 2:
				return itemstack.getItem() == ExtraPlanets_Items.POTASH_SHARDS;
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		switch (slotID) {
		case 0:
			return itemstack != null && ItemElectricBase.isElectricItem(itemstack.getItem());
		case 1:
		case 2:
			return FluidUtil.isValidContainer(itemstack);
		}

		return false;
	}

	@Override
	public boolean shouldUseEnergy() {
		return this.canProcess();
	}

	@Override
	public EnumFacing getElectricInputDirection() {
		return EnumFacing.UP;
	}

	@Override
	public EnumFacing getFront() {
		return (this.world.getBlockState(getPos()).getValue(AdvancedRefinery.FACING));
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}
}
