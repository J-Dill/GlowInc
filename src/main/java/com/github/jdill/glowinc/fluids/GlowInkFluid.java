package com.github.jdill.glowinc.fluids;

import com.github.jdill.glowinc.Registry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class GlowInkFluid extends ForgeFlowingFluid {

    private static final Properties PROPERTIES = new Properties(
            Registry.GLOW_INK_FLUID_TYPE, Registry.GLOW_INK_FLUID, Registry.GLOW_INK_FLUID_FLOWING)
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
