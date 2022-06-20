package com.github.jdill.glowinc.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class PureGlowBottleItem extends BottleItem {

    public static final String ID = "pure_glow_bottle";

    public PureGlowBottleItem() {
        super((new Item.Properties()).stacksTo(16)
                .tab(CreativeModeTab.TAB_BREWING)
                .craftRemainder(Items.GLASS_BOTTLE));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }
}
