package com.github.jdill.glowinc.blocks;

import com.github.jdill.glowinc.Config;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTicks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GlowBallBlock extends Block implements SimpleWaterloggedBlock {
    public static final String ID = "glow_ball";

    public static final int TICKS_IN_MINUTE = 1200;

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, Config.GLOW_BALL_BLOCK_MINUTES.get());
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE_U = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    protected static final VoxelShape SHAPE_D = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_N = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_S = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SHAPE_E = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_W = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
        .put(Direction.NORTH, SHAPE_N)
        .put(Direction.SOUTH, SHAPE_S)
        .put(Direction.EAST, SHAPE_E)
        .put(Direction.WEST, SHAPE_W)
        .put(Direction.UP, SHAPE_U)
        .put(Direction.DOWN, SHAPE_D)
        .build()
    );

    public GlowBallBlock() {
        super(Block.Properties.of(Material.WATER_PLANT)
            .instabreak()
            .sound(SoundType.SLIME_BLOCK)
            .noCollission()
            .lightLevel(state -> state.getValue(WATERLOGGED) ? 15 : 10) // 15 if in water, 10 otherwise
            .randomTicks()
        );
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(AGE, 0)
                        .setValue(WATERLOGGED, Boolean.FALSE)
        );
    }

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean flag) {
        super.onPlace(blockState, level, blockPos, blockState1, flag);
        level.scheduleTick(blockPos, this, TICKS_IN_MINUTE);
    }

    /*
        Going and scheduling a tick for old Glow Ball Blocks that do not have any scheduled ticks.
        This is for backwards compatibility with Glow Ball Blocks placed on v1.2.2 and before.
        TODO eventually remove the random tick functionality.
     */
    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource random) {
        LevelTicks<Block> blockTicks = level.getBlockTicks();
        if (!blockTicks.hasScheduledTick(blockPos, this)) {
            level.scheduleTick(blockPos, this, TICKS_IN_MINUTE);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if (Config.GLOW_BALL_BLOCK_PERSISTENT.get()) {
            // If Glow Ink splat is set to be persistent, don't do the tick logic.
            serverLevel.scheduleTick(blockPos, this, TICKS_IN_MINUTE);
            return;
        }

        int currentAge = blockState.getValue(AGE) + 1;
        if (currentAge >= Config.GLOW_BALL_BLOCK_MINUTES.get()) {
            // If we have reached max age, remove the block.
            serverLevel.removeBlock(blockPos, true);
        } else {
            // Increment the age of the block through the block state.
            blockState = blockState.setValue(AGE, currentAge);
            serverLevel.setBlockAndUpdate(blockPos, blockState);
        }

    }

    @Nonnull
    @Override
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    /**
     * Only allows the Glow Ball to be placed on the solid side of blocks, and only solid blocks.
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = reader.getBlockState(blockpos);
        return blockstate.isFaceSturdy(reader, blockpos, direction);
    }

    /**
     * Searches for a valid wall to place on when placed on a block.
     */
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = this.defaultBlockState();
        LevelReader levelreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidState = levelreader.getFluidState(blockpos);
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            Direction direction1 = direction.getOpposite();
            blockstate = blockstate.setValue(FACING, direction1);
            if (blockstate.canSurvive(levelreader, blockpos)) {
                return blockstate.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
            }
        }

        return null;
    }

    /**
     * When the block the Glow Ball is placed on is broken, also break the Glow Ball.
     */
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState,
        LevelAccessor levelAccessor, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }
        return facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(levelAccessor, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Nonnull
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Nonnull
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING, WATERLOGGED);
    }

}
