package com.github.jdill.glowinc.recipes;

import com.github.jdill.glowinc.Registry;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Optional;

public class InkGunRefillRecipe extends CustomRecipe {

    private final Ingredient materials;
    private final int ratio;

    public InkGunRefillRecipe(ResourceLocation p_43833_, Ingredient materials, int ratio) {
        super(p_43833_);
        this.materials = materials;
        this.ratio = ratio;
    }

    @Override
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
        int ink = 0;
        ItemStack gun = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (itemstack.getItem() == Registry.INK_GUN_ITEM.get()) {
                if (gun.isEmpty()) {
                    gun = itemstack;
                } else {
                    // Found another Ink Gun in the grid.
                    return false;
                }
            } else if (!itemstack.isEmpty()) {
                if (materials.test(itemstack)) {
                    ink++;
                } else {
                    // Unknown item in the grid.
                    return false;
                }

            } else if (itemstack != ItemStack.EMPTY) {
                return false;
            }
        }

        return gun.getDamageValue() - Mth.ceil(gun.getMaxDamage() / ratio) * ink > -Mth.ceil(gun.getMaxDamage() / ratio);
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull CraftingContainer inv) {
        int ink = 0;
        ItemStack gun = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (itemstack.getItem() == Registry.INK_GUN_ITEM.get()) {
                gun = itemstack;
            } else if (!itemstack.isEmpty()) {
                if (materials.test(itemstack)) {
                    ink++;
                }

            }
        }
        int damage = Mth.clamp(gun.getDamageValue() - Mth.ceil(gun.getMaxDamage() / ratio) * ink, 0, gun.getMaxDamage());
        ItemStack result = new ItemStack(Registry.INK_GUN_ITEM.get(), 1, gun.getTag());
        result.setDamageValue(damage);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width > 1 || height > 1;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registry.INK_GUN_REFILL.get();
    }

    @Nonnull
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, materials);
    }

    public Optional<Ingredient> getIngredient() {
        return getIngredients().stream().findFirst();
    }

    public static class Serializer implements RecipeSerializer<InkGunRefillRecipe> {

        @Override
        public InkGunRefillRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient materials = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "materials"));
            int ratio = GsonHelper.getAsInt(json, "ratio");
            return new InkGunRefillRecipe(recipeId, materials, ratio);
        }

        @Override
        public InkGunRefillRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int ratio = buffer.readInt();
            return new InkGunRefillRecipe(recipeId, ingredient, ratio);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InkGunRefillRecipe recipe) {
            recipe.getIngredient().ifPresent(ingredient -> ingredient.toNetwork(buffer));
            buffer.writeInt(recipe.ratio);
        }

    }

    public static InkGunRefillRecipe getInstance(Level level) {
        Optional<InkGunRefillRecipe> optionalRecipe = (Optional<InkGunRefillRecipe>) level.getRecipeManager().byKey(Registry.INK_GUN_REFILL.getId());
        return optionalRecipe.orElseThrow(() -> new RuntimeException("Ink Gun Refill recipe is not registered correctly."));
    }
}
