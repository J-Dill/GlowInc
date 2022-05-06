package com.github.jdill.glowinc.inventory;

import com.github.jdill.glowinc.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class SqueezerMenu extends AbstractContainerMenu {

    public static final String ID = "squeezer_menu";

    private static final int INPUT_SLOT = 0;

    private final BlockEntity blockEntity;
    private final InvWrapper playerInventory;

    public SqueezerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        super(Registry.SQUEEZER_MENU.get(), containerId);

        this.blockEntity = Minecraft.getInstance().level.getBlockEntity(extraData.readBlockPos());
        this.playerInventory = new InvWrapper(playerInventory);
        addSlots();
    }

    public SqueezerMenu(int containerId, Inventory playerInventory, BlockPos blockPos) {
        super(Registry.SQUEEZER_MENU.get(), containerId);

        this.blockEntity = Minecraft.getInstance().level.getBlockEntity(blockPos);
        this.playerInventory = new InvWrapper(playerInventory);
        addSlots();
    }

    private void addSlots() {
        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, 0,  9, 84));
        });

        int startX = 20;
        int startY = 20;
        int slotSizePlus2 = 12;
        // Main Player Inventory
        int startPlayerInvY = startY * 6;
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(this.playerInventory.getInv(), 9 + (row * 9) + column, startX + (column * slotSizePlus2),
                        startPlayerInvY + (row * slotSizePlus2)));
            }
        }

        // Hotbar
        int hotbarY = startPlayerInvY + (startPlayerInvY / 2) + 1;
        for (int column = 0; column < 9; ++column) {
            this.addSlot(new Slot(this.playerInventory.getInv(), column, startX + (column * slotSizePlus2), hotbarY));
        }
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@Nonnull Player p_38874_) {
        return true;
    }

    @Override
    public void setItem(int p_182407_, int p_182408_, ItemStack p_182409_) {
        super.setItem(p_182407_, p_182408_, p_182409_);
    }

    @Override
    public void clicked(int p_150400_, int p_150401_, ClickType p_150402_, Player p_150403_) {
        super.clicked(p_150400_, p_150401_, p_150402_, p_150403_);
    }
}
