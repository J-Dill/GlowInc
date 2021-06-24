package com.github.jdill.glowincfabric.entity.projectile;

import com.github.jdill.glowincfabric.GlowincFabric;
import com.github.jdill.glowincfabric.blocks.GlowBallBlock;
import com.github.jdill.glowincfabric.client.GlowincFabricClient;
import com.github.jdill.glowincfabric.entity.EntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GlowBallEntity extends ThrownItemEntity {

    public static final Identifier ID = new Identifier(GlowincFabric.MOD_ID, "glow_ball_entity");

    private SoundEvent soundEvent = SoundEvents.BLOCK_SLIME_BLOCK_BREAK;

    public GlowBallEntity(double d, double e, double f, World world) {
        super(GlowincFabric.GLOW_BALL_ENTITY, d, e, f, world);
    }

    public GlowBallEntity(World world) {
        super(GlowincFabric.GLOW_BALL_ENTITY, world);
    }

    public GlowBallEntity(LivingEntity livingEntity, World world) {
        super(GlowincFabric.GLOW_BALL_ENTITY, livingEntity, world);
    }

    public GlowBallEntity(EntityType<? extends ThrownItemEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return GlowincFabric.GLOW_BALL_ITEM;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.BLOCKS;
    }

    @Override
    protected void onEntityHit(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (!this.world.isClient()) {
            BlockPos hitBlockPos = result.getBlockPos();
            BlockState hitBlockState = this.world.getBlockState(hitBlockPos);
            Direction direction = result.getSide();
            BlockPos maybeBlockPos = hitBlockPos.offset(direction);
            if (hitBlockState.isSideSolidFullSquare(this.world, hitBlockPos, direction)) {
                BlockState maybeBlockState = this.world.getBlockState(maybeBlockPos);
                FluidState maybeFluidState = this.world.getFluidState(maybeBlockPos);
                boolean isWater = maybeFluidState.getFluid() == Fluids.WATER;
                if (maybeBlockState.isAir() || isWater) {
                    BlockState state = GlowincFabric.GLOW_BALL_BLOCK.getDefaultState();
                    BlockState alteredBlockState = state.with(Properties.FACING, direction).with(
                        GlowBallBlock.WATERLOGGED, isWater);
                    this.world.setBlockState(maybeBlockPos, alteredBlockState);
                    soundEvent = SoundEvents.BLOCK_SLIME_BLOCK_PLACE;
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private ParticleEffect makeParticle() {
        return new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack());
    }

    @Environment(EnvType.CLIENT)
    public void handleStatus(byte id) {
        if (id == 3) {
            ParticleEffect particle = makeParticle();
            int numParticles = 5;
            for (int i = 0; i < numParticles; ++i) {
                this.world
                    .addParticle(particle, this.getX(), this.getY(), this.getZ(),
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.1D) * 0.08D,
                        ((double) this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient()) {
            this.world.sendEntityStatus(this, (byte) 3);
            this.playSound(soundEvent, 0.8f, 0.8f);
            this.discard();
        }
    }

    @Override
    public boolean isTouchingWater() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Packet createSpawnPacket() {
        return EntitySpawnPacket.create(this, GlowincFabricClient.PACKET_ID);
    }

}
