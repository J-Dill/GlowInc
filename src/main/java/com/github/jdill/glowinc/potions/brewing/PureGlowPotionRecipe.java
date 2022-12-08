package com.github.jdill.glowinc.potions.brewing;

import com.github.jdill.glowinc.potions.PureGlowBottlePotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.crafting.NBTIngredient;

public class PureGlowPotionRecipe extends BrewingRecipe {

    public static final Ingredient ALLOWED_INPUT = new NBTIngredient(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)){};
    public static final Ingredient ALLOWED_INGREDIENT = Ingredient.of(Items.GLOW_INK_SAC);

    public PureGlowPotionRecipe() {
        super(ALLOWED_INPUT, ALLOWED_INGREDIENT, PureGlowBottlePotion.getPureGlowPotion());
    }

    public static class PureGlowPotionIngredient extends NBTIngredient {

        public PureGlowPotionIngredient() {
            super(PureGlowBottlePotion.getPureGlowPotion());
        }

    }

}
