package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.blockentity.SqueezerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SqueezerBlock extends BaseEntityBlock {

    public static final String ID = "squeezer_block";

    public static final IntegerProperty PROGRESS = IntegerProperty.create("squeeze_progress", 0, 4);

    public SqueezerBlock() {
        super(Properties.of(Material.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(PROGRESS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PROGRESS);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void fallOn(@Nonnull Level level, @Nonnull BlockState blockState, @Nonnull BlockPos blockPos,
                       @Nonnull Entity entity, float speed) {
        level.setBlock(blockPos, blockState.cycle(PROGRESS), 3);
        super.fallOn(level, blockState, blockPos, entity, speed);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return new SqueezerBlockEntity(blockPos, blockState);
    }

}
