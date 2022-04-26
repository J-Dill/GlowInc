package com.github.jdill.glowinc.items;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class PureGlowBottleColor implements ItemColor {

    private static final int LIQUID_COLOR = 65522;

    @Override
    public int getColor(@Nonnull ItemStack stack, int layer) {
        return layer == 0 ? LIQUID_COLOR : -1;
    }

}
