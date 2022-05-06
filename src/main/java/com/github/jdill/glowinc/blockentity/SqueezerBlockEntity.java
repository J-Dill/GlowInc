package com.github.jdill.glowinc.blockentity;

import com.github.jdill.glowinc.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class SqueezerBlockEntity extends BaseContainerBlockEntity {

    public static final String ID = "squeezer_block_entity";

    public SqueezerBlockEntity(BlockPos p_155077_, BlockState p_155078_) {
        super(Registry.SQUEEZER_BLOCK_ENTITY.get(), p_155077_, p_155078_);
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory inv) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getItem(int p_18941_) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return null;
    }

    @Override
    public void setItem(int p_18944_, @Nonnull ItemStack p_18945_) {

    }

    @Override
    public boolean stillValid(@Nonnull Player p_18946_) {
        return false;
    }

    @Override
    public void clearContent() {

    }
}
