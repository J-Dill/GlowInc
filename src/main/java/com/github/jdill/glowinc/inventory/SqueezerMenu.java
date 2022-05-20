package com.github.jdill.glowinc.inventory;

import com.github.jdill.glowinc.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SqueezerMenu extends RecipeBookMenu<Container> {

    public static final String ID = "squeezer_menu";

    private static final int INPUT_SLOT = 0;
    private final InvWrapper playerInventory;
    private final BlockEntity blockEntity;

    public SqueezerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(Registry.SQUEEZER_MENU.get(), containerId);

        this.blockEntity = Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos());
        this.playerInventory = new InvWrapper(playerInventory);
    }

    public SqueezerMenu(int containerId, Inventory playerInventory, BlockPos blockPos) {
        super(Registry.SQUEEZER_MENU.get(), containerId);

        this.blockEntity = Minecraft.getInstance().level.getBlockEntity(blockPos);
        this.playerInventory = new InvWrapper(playerInventory);
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {

    }

    @Override
    public void clearCraftingContent() {
        this.getSlot(INPUT_SLOT).set(ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super Container> p_40118_) {
        return false;
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 0;
    }

    @Override
    public int getGridHeight() {
        return 0;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int p_150635_) {
        return false;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }
}
