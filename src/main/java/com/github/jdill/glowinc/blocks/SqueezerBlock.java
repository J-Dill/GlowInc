package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.blockentity.SqueezerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class SqueezerBlock extends BaseEntityBlock {

    public static final String ID = "squeezer_block";

    public SqueezerBlock() {
        super(Properties.of(Material.WOOD));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState blockState) {
        return new SqueezerBlockEntity(blockPos, blockState);
    }

}
