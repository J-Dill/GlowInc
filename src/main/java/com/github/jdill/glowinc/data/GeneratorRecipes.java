package com.github.jdill.glowinc.data;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.potions.brewing.PureGlowPotionRecipe;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class GeneratorRecipes extends RecipeProvider {

    public GeneratorRecipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.item();
        builder.isPotion(Registry.PURE_GLOW_POTION.get());
        ItemPredicate itemPredicate = builder.build();

        InventoryChangeTrigger.TriggerInstance triggerInstance = InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate);

        ShapelessRecipeBuilder.shapeless(Items.GLOWSTONE_DUST)
                .requires(new PureGlowPotionRecipe.PureGlowPotionIngredient(), 1)
                .unlockedBy("has_glow_potion", triggerInstance)
                .save(consumer);
    }
}
