package com.mjr.extraplanets.entities.bosses.defaultBosses;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.EntityAIArrowAttack;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityProjectileTNT;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mjr.extraplanets.items.ExtraPlanets_Items;

public class EntityCreeperBossNeptune extends EntityBossBase implements IEntityBreathable, IRangedAttackMob {
	protected long ticks = 0;
	public int headsRemaining = 3;
	private Entity targetEntity;

	public EntityCreeperBossNeptune(World par1World) {
		super(par1World);
		this.setSize(2.0F, 7.0F);
		this.isImmuneToFire = true;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 25, 20.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, null));
	}

	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float damage) {
		if (damageSource.getDamageType().equals("fireball")) {
			if (this.isEntityInvulnerable(damageSource)) {
				return false;
			} else if (super.attackEntityFrom(damageSource, damage)) {
				Entity entity = damageSource.getEntity();

				if (this.getPassengers().contains(entity) && this.getRidingEntity() != entity) {
					if (entity != this && entity instanceof EntityLivingBase) {
						this.setAttackTarget((EntityLivingBase) entity);
					}

					return true;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}

		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0F * ConfigManagerCore.dungeonBossHealthMod);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.05F);
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5) {
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return null;
	}

	@Override
	protected SoundEvent getHurtSound() {
		this.playSound(GCSounds.bossOuch, this.getSoundVolume(), this.getSoundPitch() - 0.15F);
		return null;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return null;
	}

	// @Override
	// protected String getHurtSound()
	// {
	// this.playSound(Constants.TEXTURE_PREFIX + "entity.ouch", this.getSoundVolume(), this.getSoundPitch() - 0.15F);
	// return null;
	// }
	//
	// @Override
	// protected String getDeathSound()
	// {
	// return null;
	// }

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		if (!this.world.isRemote) {
			if (this.deathTicks == 1) {
				GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, this.world.provider.getDimension(), new Object[] { getSoundPitch() - 0.1F }),
						new TargetPoint(this.world.provider.getDimension(), this.posX, this.posY, this.posZ, 40.0D));
			}
		}
	}

	@Override
	public void onLivingUpdate() {
		if (this.ticks >= Long.MAX_VALUE) {
			this.ticks = 1;
		}

		this.ticks++;

		if (this.getHealth() <= 0) {
			this.headsRemaining = 0;
		} else if (this.getHealth() <= this.getMaxHealth() / 3.0) {
			this.headsRemaining = 1;
		} else if (this.getHealth() <= 2 * (this.getMaxHealth() / 3.0)) {
			this.headsRemaining = 2;
		}

		final EntityPlayer player = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0, false);

		if (player != null && !player.equals(this.targetEntity)) {
			if (this.getDistanceSqToEntity(player) < 400.0D) {
				this.getNavigator().getPathToEntityLiving(player);
				this.targetEntity = player;
			}
		} else {
			this.targetEntity = null;
		}

		super.onLivingUpdate();
	}

	@Override
	protected Item getDropItem() {
		return Items.ARROW;
	}

	@Override
	public EntityItem entityDropItem(ItemStack par1ItemStack, float par2) {
		final EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + par2, this.posZ, par1ItemStack);
		entityitem.motionY = -2.0D;
		entityitem.setDefaultPickupDelay();
		if (this.captureDrops) {
			this.capturedDrops.add(entityitem);
		} else {
			this.world.spawnEntity(entityitem);
		}
		return entityitem;
	}

	@Override
	protected void dropFewItems(boolean b, int i) {
		if (this.rand.nextInt(200) - i >= 5) {
			return;
		}

		if (i > 0) {
			final ItemStack var2 = new ItemStack(Items.BOW);
			EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5, false);
			this.entityDropItem(var2, 0.0F);
		} else {
			this.dropItem(Items.BOW, 1);
		}
	}

	@Override
	public boolean canBreath() {
		return true;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
		this.world.playEvent(null, 1024, new BlockPos(this), 0);
		double d3 = this.posX;
		double d4 = this.posY + 5.5D;
		double d5 = this.posZ;
		double d6 = entitylivingbase.posX - d3;
		double d7 = entitylivingbase.posY + entitylivingbase.getEyeHeight() * 0.5D - d4;
		double d8 = entitylivingbase.posZ - d5;
		EntityProjectileTNT projectileTNT = new EntityProjectileTNT(this.world, this, d6 * 0.5D, d7 * 0.5D, d8 * 0.5D);

		projectileTNT.posY = d4;
		projectileTNT.posX = d3;
		projectileTNT.posZ = d5;
		this.world.spawnEntity(projectileTNT);
	}

	@Override
	public ItemStack getGuaranteedLoot(Random rand) {
		List<ItemStack> stackList = GalacticraftRegistry.getDungeonLoot(9);
		return stackList.get(rand.nextInt(stackList.size())).copy();
	}

	@Override
	public int getChestTier() {
		return 8;
	}

	@Override
	public void dropKey() {
		this.entityDropItem(new ItemStack(ExtraPlanets_Items.TIER_8_KEY, 1, 0), 0.5F);
	}

	@Override
	public BossInfo.Color getHealthBarColor() {
		return BossInfo.Color.YELLOW;
	}
}