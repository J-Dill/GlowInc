package com.github.jdill.glowinc.entity.projectile;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.blocks.GlowBallBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nonnull;

//@OnlyIn(
//    value = Dist.CLIENT,
//    _interface = IItemRenderProperties.class
//)
public class GlowBallEntity extends ThrowableItemProjectile {

    public static final String ID = "glow_ball_entity";

    private SoundEvent soundEvent = SoundEvents.SLIME_BLOCK_BREAK;

    public GlowBallEntity(LivingEntity livingEntityIn, Level worldIn) {
        super(Registry.GLOW_BALL_ENTITY.get(), livingEntityIn, worldIn);
    }

    public GlowBallEntity(EntityType<GlowBallEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public GlowBallEntity(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z,
        Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    @Nonnull
    @Override
    protected Item getDefaultItem() {
        return Registry.GLOW_BALL_ITEM.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (!this.level.isClientSide() && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level.isClientSide()) {
            BlockPos hitBlockPos = result.getBlockPos();
            BlockState hitBlockState = this.level.getBlockState(hitBlockPos);
            Direction direction = result.getDirection();
            BlockPos maybeBlockPos = hitBlockPos.relative(direction);
            if (hitBlockState.isFaceSturdy(this.level, hitBlockPos, direction)) {
                BlockState maybeBlockState = this.level.getBlockState(maybeBlockPos);
                FluidState maybeFluidState = this.level.getFluidState(maybeBlockPos);
                boolean isWater = maybeFluidState.getType() == Fluids.WATER;
                if (maybeBlockState.isAir() || isWater) {
                    BlockState state = Registry.GLOW_BALL_BLOCK.get().defaultBlockState();
                    BlockState alteredBlockState = state.setValue(BlockStateProperties.FACING, direction).setValue(
                            GlowBallBlock.WATERLOGGED, isWater);
                    this.level.setBlockAndUpdate(maybeBlockPos, alteredBlockState);
                    soundEvent = SoundEvents.SLIME_BLOCK_PLACE;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private ParticleOptions makeParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            int numParticles = 5;
            for (int i = 0; i < numParticles; ++i) {
                this.level
                    .addParticle(makeParticle(), this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.1D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        if (!this.level.isClientSide()) {
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.playSound(soundEvent, 0.8f, 0.8f);
            this.discard();
        }
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
