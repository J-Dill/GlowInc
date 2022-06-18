package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nonnull;

public class GlowInkFluidBlock extends LiquidBlock implements IFluidBlock {

    public static final String ID = "glow_ink_fluid_block";

    // TODO: better configure fluid in world (aka all of this class)
    public GlowInkFluidBlock() {
        super(Registry.GLOW_INK_FLUID_FLOWING, Properties.of(Material.WATER));
    }

    @Override
    public int place(Level world, BlockPos pos,
                     @Nonnull FluidStack fluidStack, FluidAction action) {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(Level world, BlockPos pos, FluidAction action) {
        return null;
    }

    @Override
    public boolean canDrain(Level world, BlockPos pos) {
        return false;
    }

    @Override
    public float getFilledPercentage(Level world, BlockPos pos) {
        return 0;
    }
}
