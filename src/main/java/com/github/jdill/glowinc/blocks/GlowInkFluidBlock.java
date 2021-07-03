package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.Registry;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;

public class GlowInkFluidBlock extends FlowingFluidBlock {

    public static final String ID = "glow_ink_fluid_block";

    // TODO: better configure fluid in world
    public GlowInkFluidBlock() {
        super(Registry.GLOW_INK_FLUID,
            Properties.create(Material.WATER)
                .doesNotBlockMovement()
                .hardnessAndResistance(100.0F)
                .noDrops()
        );
    }
}
