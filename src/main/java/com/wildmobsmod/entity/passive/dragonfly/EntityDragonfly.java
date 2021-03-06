package com.wildmobsmod.entity.passive.dragonfly;

import java.util.Calendar;

import com.wildmobsmod.items.WildMobsModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDragonfly extends EntityAmbientCreature
{
    private ChunkCoordinates spawnPosition;

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(20, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(21, Byte.valueOf((byte)0));
	}
	
    public EntityDragonfly(World p_i1680_1_)
    {
        super(p_i1680_1_);
        this.setSize(0.5F, 0.3F);
        this.setIsHovering(false);
    }
	
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData entity)
	{
		super.onSpawnWithEgg(entity);
        this.setSkin(this.worldObj.rand.nextInt(6));
		return entity;
	}
	
    public void writeEntityToNBT(NBTTagCompound entity)
    {
        super.writeEntityToNBT(entity);
        entity.setInteger("Variant", this.getSkin());
        entity.setBoolean("IsHovering", this.getIsHovering());
    }

    public void readEntityFromNBT(NBTTagCompound entity)
    {
        super.readEntityFromNBT(entity);
        this.setSkin(entity.getInteger("Variant"));
        this.setIsHovering(entity.getBoolean("IsHovering"));
    }
    
    public boolean getIsHovering()
    {
        return (this.dataWatcher.getWatchableObjectByte(21) & 1) != 0;
    }

    public void setIsHovering(boolean p_70900_1_)
    {
        if (p_70900_1_)
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)1));
        }
        else
        {
            this.dataWatcher.updateObject(21, Byte.valueOf((byte)0));
        }
    }

    public int getSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(20);
    }

    public void setSkin(int entity)
    {
        this.dataWatcher.updateObject(20, Byte.valueOf((byte)entity));
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.1F;
    }
    
    protected String getLivingSound()
    {
        return "wildmobsmod:mob.dragonfly.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return null;
    }

    protected String getDeathSound()
    {
        return null;
    }

    public boolean canBePushed()
    {
        return false;
    }

    protected void collideWithEntity(Entity p_82167_1_) {}

    protected void collideWithNearbyEntities() {}

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2.0D);
    }

    protected boolean isAIEnabled()
    {
        return true;
    }

    public void onUpdate()
    {
    	super.onUpdate();
    	if (this.rand.nextFloat() < 0.12F && this.getIsHovering() == true)
    	{
    		this.setIsHovering(false);
    	}
    	else if (this.rand.nextFloat() < 0.06F && this.getIsHovering() == false)
    	{
    		this.setIsHovering(true);
    	}
    	if (this.getIsHovering() == false)
    	{
        	this.motionY *= 0.6000000238418579D;
    	}
    	else
    	{
        	this.motionY *= 0.0D;  		
    	}
    }

    protected void updateAITasks()
    {
    	super.updateAITasks();
    	
    		if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition.posX, this.spawnPosition.posY, this.spawnPosition.posZ) || this.spawnPosition.posY < 1))
    		{
    			this.spawnPosition = null;
    		}

    		if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F)
    		{
    			this.spawnPosition = new ChunkCoordinates((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
    		}

			double d0 = (double)this.spawnPosition.posX + 0.5D - this.posX;
			double d1 = (double)this.spawnPosition.posY + 0.1D - this.posY;
			double d2 = (double)this.spawnPosition.posZ + 0.5D - this.posZ;
			
    		if (this.getIsHovering() == false)
    		{
    			this.motionX += (Math.signum(d0) * 0.3D - this.motionX) * 0.15000000149011612D;
    			this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.15000000149011612D;
    			this.motionZ += (Math.signum(d2) * 0.3D - this.motionZ) * 0.15000000149011612D;
    			float f = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;
    			float f1 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
    			this.moveForward = 0.5F;
    			this.rotationYaw += f1;
    		}
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void fall(float p_70069_1_) {}

    protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {}

    public boolean doesEntityNotTriggerPressurePlate()
    {
        return true;
    }

    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            
            return super.attackEntityFrom(p_70097_1_, p_70097_2_);
        }
    }
    
    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();
        int i = this.getSkin();
        
        if (super.interact(p_70085_1_))
        {
            return true;
        }
        else if (itemstack != null && itemstack.getItem() == WildMobsModItems.bugNet)
        {
        	ItemStack itemstack1 = new ItemStack(WildMobsModItems.dragonfly, 1, i);
            if (!p_70085_1_.inventory.addItemStackToInventory(itemstack1))
            {
                p_70085_1_.dropPlayerItemWithRandomChoice(itemstack1, false);
                this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            	itemstack.damageItem(1, p_70085_1_);
            	this.isDead = true;
                return true;
            }
            else
            {
                this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            	itemstack.damageItem(1, p_70085_1_);
            	this.isDead = true;
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean getCanSpawnHere()
    {
    	if (this.worldObj.rand.nextInt(2) == 0)
    	{
    		return false;
    	}
    	else
    	{
    		int i = MathHelper.floor_double(this.posX);
    		int j = MathHelper.floor_double(this.boundingBox.minY);
    		int k = MathHelper.floor_double(this.posZ);
    		return this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    	}
    }
}