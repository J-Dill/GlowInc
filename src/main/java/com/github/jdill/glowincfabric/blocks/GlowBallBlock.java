package com.github.jdill.glowincfabric.blocks;

import com.github.jdill.glowincfabric.GlowincFabric;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class GlowBallBlock extends Block implements Waterloggable {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final VoxelShape SHAPE_U = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    protected static final VoxelShape SHAPE_D = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_N = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_S = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SHAPE_E = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_W = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
        .put(Direction.NORTH, SHAPE_N)
        .put(Direction.SOUTH, SHAPE_S)
        .put(Direction.EAST, SHAPE_E)
        .put(Direction.WEST, SHAPE_W)
        .put(Direction.UP, SHAPE_U)
        .put(Direction.DOWN, SHAPE_D)
        .build()
    );

    private static final DirectionProperty FACING = Properties.FACING;

    public static final Identifier ID = new Identifier(GlowincFabric.MOD_ID, "glow_ball");

    public GlowBallBlock() {
        super(FabricBlockSettings.of(Material.REPLACEABLE_UNDERWATER_PLANT)
            .breakInstantly()
            .sounds(BlockSoundGroup.SLIME)
            .breakByHand(true)
            .collidable(false)
            .nonOpaque()
            .luminance((state) -> 14) // TODO: Make bright under water?
        );
//        this.setDefaultState(this.getStateContainer().getBaseState().with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos,
        ShapeContext context) {
        return SHAPES.get(state.get(FACING));
    }

    /**
     * Only allows the Glow Ball to be placed on the solid side of blocks, and only solid blocks.
     */
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState blockstate = world.getBlockState(blockpos);
        return blockstate.isSideSolidFullSquare(world, blockpos, direction);
    }

    /**
     * Searches for a valid wall to place on when placed on a block.
     */
    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockstate = this.getDefaultState();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);
        Direction[] adirection = context.getPlacementDirections();

        for(Direction direction : adirection) {
            Direction direction1 = direction.getOpposite();
            blockstate = blockstate.with(FACING, direction1);
            if (blockstate.canPlaceAt(world, blockPos)) {
                return blockstate.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }

        return null;
    }

    /**
     * When the block the Glow Ball is placed on is broken, also break the Tree Tap.
     */
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction,
        BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }


    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

}
