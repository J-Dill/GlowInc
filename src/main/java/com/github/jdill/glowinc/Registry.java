package com.github.jdill.glowinc;

import com.github.jdill.glowinc.blocks.GlowInkBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GlowInc.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> GLOW_INK_BLOCK = BLOCKS.register(GlowInkBlock.ID, GlowInkBlock::new);

}
