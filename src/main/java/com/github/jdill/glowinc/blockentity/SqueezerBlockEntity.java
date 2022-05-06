package com.github.jdill.glowinc.blockentity;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.inventory.SqueezerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SqueezerBlockEntity extends BaseContainerBlockEntity implements MenuProvider {

    public static final String ID = "squeezer_block_entity";

    protected NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);

    public SqueezerBlockEntity(BlockPos p_155077_, BlockState p_155078_) {
        super(Registry.SQUEEZER_BLOCK_ENTITY.get(), p_155077_, p_155078_);
    }

    @Nonnull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.squeezer");
    }

    @Nonnull
    @Override
    protected AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory inv) {
        return new SqueezerMenu(containerId, inv, this.worldPosition);
    }

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.inventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot) {
        return this.inventory.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int number) {
        return ContainerHelper.removeItem(this.inventory, slot, number);
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.inventory.set(slot, itemStack);
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return Items.GLOW_INK_SAC == itemStack.getItem();
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.inventory);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.inventory);
    }
}
