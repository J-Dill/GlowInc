package com.github.jdill.glowinc.fluids;

import com.github.jdill.glowinc.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class GlowInkFluid extends ForgeFlowingFluid {

    private static final FluidAttributes.Builder ATTRIBUTES_BUILDER = FluidAttributes.builder(
            new ResourceLocation("glowinc:blocks/" + Source.ID),
            new ResourceLocation("glowinc:blocks/" + Flowing.ID))
        .density(3000)
        .viscosity(6000);
    private static final Properties PROPERTIES = new Properties(
            Registry.GLOW_INK_FLUID, Registry.GLOW_INK_FLUID_FLOWING, ATTRIBUTES_BUILDER)
        .bucket(Registry.GLOW_INK_BUCKET_ITEM)
        .block(Registry.GLOW_INK_BLOCK)
        .levelDecreasePerBlock(2)
        .tickRate(20);

    protected GlowInkFluid() {
        super(PROPERTIES);
    }

    public static class Flowing extends ForgeFlowingFluid.Flowing {
        public static final String ID = "glow_ink_fluid_flowing";

        public Flowing() {
            super(PROPERTIES);
        }
    }

    public static class Source extends ForgeFlowingFluid.Source {
        public static final String ID = "glow_ink_fluid_still";

        public Source() {
            super(PROPERTIES);
        }

    }
}
