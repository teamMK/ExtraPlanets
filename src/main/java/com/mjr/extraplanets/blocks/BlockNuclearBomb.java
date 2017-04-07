package com.mjr.extraplanets.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.mjr.extraplanets.entities.EntityNuclearBombPrimed;

public class BlockNuclearBomb extends Block {
	public static final PropertyBool EXPLODE = PropertyBool.create("explode");

	public BlockNuclearBomb() {
		super(Material.TNT);
		this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);

		if (worldIn.isBlockPowered(pos)) {
			this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)));
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (worldIn.isBlockPowered(pos)) {
			this.onBlockDestroyedByPlayer(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)));
			worldIn.setBlockToAir(pos);
		}
	}

	/**
	 * Called when this Block is destroyed by an Explosion
	 */
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		if (!worldIn.isRemote) {
			EntityNuclearBombPrimed EntityNuclearBombPrimed = new EntityNuclearBombPrimed(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, explosionIn.getExplosivePlacedBy());
			EntityNuclearBombPrimed.fuse = worldIn.rand.nextInt(EntityNuclearBombPrimed.fuse / 4) + EntityNuclearBombPrimed.fuse / 8;
			worldIn.spawnEntity(EntityNuclearBombPrimed);
		}
	}

	/**
	 * Called when a player destroys this Block
	 */
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		this.explode(worldIn, pos, state, (EntityLivingBase) null);
	}

	public void explode(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!worldIn.isRemote) {
			if (state.getValue(EXPLODE).booleanValue()) {
				EntityNuclearBombPrimed EntityNuclearBombPrimed = new EntityNuclearBombPrimed(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, igniter);
				worldIn.spawnEntity(EntityNuclearBombPrimed);
				worldIn.playSound((EntityPlayer) null, EntityNuclearBombPrimed.posX, EntityNuclearBombPrimed.posY, EntityNuclearBombPrimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.inventory.getCurrentItem() != null) {
			Item item = playerIn.inventory.getCurrentItem().getItem();

			if (item == Items.FLINT_AND_STEEL || item == Items.FIRE_CHARGE) {
				this.explode(worldIn, pos, state.withProperty(EXPLODE, Boolean.valueOf(true)), playerIn);
				worldIn.setBlockToAir(pos);

				if (item == Items.FLINT_AND_STEEL) {
					playerIn.inventory.getCurrentItem().damageItem(1, playerIn);
				} else if (!playerIn.capabilities.isCreativeMode) {
					--playerIn.inventory.getCurrentItem().stackSize;
				}

				return true;
			}
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

	/**
	 * Called When an Entity Collided with the Block
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
			EntityArrow entityarrow = (EntityArrow) entityIn;

			if (entityarrow.isBurning()) {
				this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	@Override
	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(EXPLODE, Boolean.valueOf((meta & 1) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(EXPLODE).booleanValue() ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { EXPLODE });
	}
}