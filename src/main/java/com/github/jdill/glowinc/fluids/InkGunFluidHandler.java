package com.github.jdill.glowinc.fluids;

import com.github.jdill.glowinc.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class InkGunFluidHandler extends FluidHandlerItemStack {

    protected static final FluidStack EMPTY = new FluidStack(Registry.GLOW_INK_FLUID.get(), 0);

    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public InkGunFluidHandler(@Nonnull ItemStack container, int capacity) {
        super(container, capacity);
        if (getFluid() == null) {
            setContainerToEmpty();
        }
    }

    @Override
    protected void setContainerToEmpty() {
        setFluid(EMPTY.copy());
        container.getOrCreateTag().remove(FLUID_NBT_KEY);
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return fluid.getFluid() == Registry.GLOW_INK_FLUID.get();
    }
}
