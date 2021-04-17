package com.github.jdill.glowinc.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class GlowBallBlock extends Block {

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public static final String ID = "glow_ball";

    public GlowBallBlock() {
        super(Block.Properties.create(Material.OCEAN_PLANT)
            .zeroHardnessAndResistance()
            .sound(SoundType.SLIME)
            .harvestLevel(0)
            .doesNotBlockMovement()
            .notSolid()
        );
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
