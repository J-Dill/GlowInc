package com.github.jdill.glowinc.fluids;

import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

public class GlowInkFluidType extends FluidType {

    public static final String ID = "glow_ink_fluid";

    private static final Properties PROPERTIES = FluidType.Properties.create()
            .fallDistanceModifier(0F)
            .canExtinguish(true)
            .supportsBoating(true)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH);

    public GlowInkFluidType() {
        super(PROPERTIES);
    }
}
