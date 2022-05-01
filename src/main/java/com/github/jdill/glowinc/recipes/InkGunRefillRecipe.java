package com.github.jdill.glowinc.recipes;

import com.github.jdill.glowinc.Registry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InkGunRefillRecipe extends CustomRecipe {

    private final String group;
    private final Item repairable;
    private final List<Ingredient> material;
    private final int ratio;

    public InkGunRefillRecipe(ResourceLocation p_43833_, String group, Item repairable, List<Ingredient> material, int ratio) {
        super(p_43833_);
        this.group = group;
        this.repairable = repairable;
        this.material = material;
        this.ratio = ratio;
    }

    @Override
    public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
        int ink = 0;
        ItemStack gun = ItemStack.EMPTY;

        for (int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack itemstack = inv.getItem(i);
            if (itemstack.getItem() == repairable) {
                if (gun.isEmpty()) {
                    gun = itemstack;
                } else {
                    return false;
                }
            } else if (!itemstack.isEmpty()) {
                for (Ingredient ingredient : material) {
                    if (ingredient.test(itemstack)) {
                        ink++;
                        break;
                    } else {
                        // Unknown item in crafting grid.
                        return false;
                    }
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
            if (itemstack.getItem() == repairable) {
                gun = itemstack;
            } else if (!itemstack.isEmpty()) {
                for (Ingredient ingredient : material) {
                    if (ingredient.test(itemstack)) {
                        ink++;
                        break;
                    }
                }
            }
        }
        int damage = Mth.clamp(gun.getDamageValue() - Mth.ceil(gun.getMaxDamage() / ratio) * ink, 0, gun.getMaxDamage());
        ItemStack result = new ItemStack(repairable, 1, gun.getTag());
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

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InkGunRefillRecipe> {
        @Nonnull
        @Override
        public InkGunRefillRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            String s = GsonHelper.getAsString(json, "repairable");
            Item repairable = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
            if (repairable == null) {
                throw new JsonSyntaxException("Unknown item '" + s + "'");
            }
            JsonArray materials = GsonHelper.getAsJsonArray(json, "material");
            List<Ingredient> ingredients = new ArrayList<>(Collections.emptyList());
            for (JsonElement material : materials) {
                ingredients.add(Ingredient.fromJson(material));
            }
            int ratio = GsonHelper.getAsInt(json, "ratio");
            return new InkGunRefillRecipe(recipeId, group, repairable, ingredients, ratio);
        }

        @Override
        public InkGunRefillRecipe fromNetwork(@Nonnull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(256);
            Item repairable = Item.byId(buffer.readVarInt());
            Ingredient material = Ingredient.fromNetwork(buffer);
            List<Ingredient> ingredients = new ArrayList<>(Collections.emptyList());
            for (ItemStack item : material.getItems()) {
                ingredients.add(Ingredient.of(item));
            }
            int ratio = buffer.readVarInt();
            return new InkGunRefillRecipe(recipeId, group, repairable, ingredients, ratio);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InkGunRefillRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(Item.getId(recipe.repairable));
            Ingredient material = Ingredient.fromNetwork(buffer);
            for (ItemStack item : material.getItems()) {
                Ingredient.of(item).toNetwork(buffer);
            }
            buffer.writeVarInt(recipe.ratio);
        }

    }
}
