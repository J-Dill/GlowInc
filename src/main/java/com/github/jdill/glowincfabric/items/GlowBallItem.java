package com.github.jdill.glowincfabric.items;

import com.github.jdill.glowincfabric.GlowincFabric;
import com.github.jdill.glowincfabric.entity.projectile.GlowBallEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GlowBallItem extends BlockItem {

    public GlowBallItem() {
        super(GlowincFabric.GLOW_BALL_BLOCK, new FabricItemSettings().group(ItemGroup.MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getStackInHand(handIn);
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClient()) {
            GlowBallEntity glowBallEntity = new GlowBallEntity(playerIn, worldIn);
            glowBallEntity.setProperties(playerIn, playerIn.getPitch(), playerIn.getYaw(), 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(glowBallEntity);
        }

        playerIn.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!playerIn.getAbilities().creativeMode) {
            itemstack.decrement(1);
        }

        return TypedActionResult.success(itemstack, worldIn.isClient());
    }

}
