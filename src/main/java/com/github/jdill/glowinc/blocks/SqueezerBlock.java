package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.blockentity.SqueezerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof MenuProvider) {
                NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

}
