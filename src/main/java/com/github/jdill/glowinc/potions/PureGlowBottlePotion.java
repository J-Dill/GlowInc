package com.github.jdill.glowinc.potions;

import com.github.jdill.glowinc.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class PureGlowBottlePotion extends Potion {

    public static final String ID = "pure_glow";

    public PureGlowBottlePotion() {
        super("pure_glow", new MobEffectInstance(MobEffects.GLOWING, 200));
    }

    public static ItemStack getPureGlowPotion() {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), Registry.PURE_GLOW_POTION.get()).copy();
    }

}
