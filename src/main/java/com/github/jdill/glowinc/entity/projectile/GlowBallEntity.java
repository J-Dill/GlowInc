package com.github.jdill.glowinc.entity.projectile;

import com.github.jdill.glowinc.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class GlowBallEntity extends ProjectileItemEntity implements IRendersAsItem {

    public static final String ID = "glow_ball_entity";

    public GlowBallEntity(LivingEntity livingEntityIn, World worldIn) {
        super(Registry.SAP_ENTITY.get(), livingEntityIn, worldIn);
    }

    public GlowBallEntity(EntityType<GlowBallEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public GlowBallEntity(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z,
        World worldIn) {
        super(type, x, y, z, worldIn);
    }

    @Override
    protected Item getDefaultItem() {
        return Registry.GLOW_BALL_ITEM.get();
    }

    // TODO add glowing effect on entity hit

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
        super.func_230299_a_(result);
        if (!this.world.isRemote) {
            BlockPos hitBlockPos = result.getPos();
            BlockState hitBlockState = this.world.getBlockState(hitBlockPos);
            Direction direction = result.getFace();
            BlockPos maybeBlockPos = hitBlockPos.offset(direction);
            BlockState blockState = this.world.getBlockState(maybeBlockPos);
            // TODO spawn particles and sound if isn't solid
            if (hitBlockState.isSolidSide(this.world, hitBlockPos, direction) && blockState.isAir()) {
                BlockState state = Registry.GLOW_BALL_BLOCK.get().getDefaultState();
                BlockState alteredBlockState = state.with(BlockStateProperties.FACING, direction);
                this.world.setBlockState(maybeBlockPos, alteredBlockState);
                this.world.playSound(maybeBlockPos.getX(), maybeBlockPos.getY(), maybeBlockPos.getZ(),
                    SoundEvents.BLOCK_SLIME_BLOCK_PLACE, SoundCategory.BLOCKS,
                    1.0f, 1.0f, true
                );
                this.remove();
            }
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
