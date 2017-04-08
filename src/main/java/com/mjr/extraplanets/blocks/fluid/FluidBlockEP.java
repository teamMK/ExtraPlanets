package com.mjr.extraplanets.blocks.fluid;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidBlockEP extends BlockFluidClassic {

	public FluidBlockEP(Fluid fluid, String assetName, Material material) {
		super(fluid, material);
		this.setQuantaPerBlock(9);
		this.needsRandomTick = true;
		this.setUnlocalizedName(assetName);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (state == ExtraPlanets_Fluids.magma) {
			if ((entityIn instanceof EntityLivingBase)) {
				if (worldIn.getTotalWorldTime() % 8 == 0 && entityIn instanceof EntityLivingBase && !((EntityLivingBase) entityIn).isEntityUndead()) {
					((EntityLivingBase) entityIn).attackEntityFrom(DamageSource.LAVA, 4.0F);
					((EntityLivingBase) entityIn).setFire(15);
				}
			}
		}
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid()) {
			return false;
		}

		return super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid()) {
			return false;
		}

		return super.displaceIfPossible(world, pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);

		if (rand.nextInt(1200) == 0) {
			worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
		}
		if (rand.nextInt(10) == 0) {
			BlockPos below = pos.down();
			IBlockState state = worldIn.getBlockState(below);
			if (state.getBlock().isSideSolid(state, worldIn, below, EnumFacing.UP) && !worldIn.getBlockState(pos.down(2)).getBlock().getMaterial(worldIn.getBlockState(pos)).blocksMovement()) {
				GalacticraftCore.proxy.spawnParticle("", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0), new Object[] {});
			}
		}
	}
}