package com.github.jdill.glowinc;

import com.github.jdill.glowinc.blocks.GlowBallBlock;
import com.github.jdill.glowinc.blocks.GlowInkFluidBlock;
import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import com.github.jdill.glowinc.fluids.GlowInkFluid;
import com.github.jdill.glowinc.items.GlowBallItem;
import com.github.jdill.glowinc.items.GlowInkBucketItem;
import com.github.jdill.glowinc.items.InkGunItem;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GlowInc.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GlowInc.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, GlowInc.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, GlowInc.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> GLOW_BALL_BLOCK = BLOCKS.register(GlowBallBlock.ID, GlowBallBlock::new);
    public static final RegistryObject<LiquidBlock> GLOW_INK_BLOCK = BLOCKS.register(
        GlowInkFluidBlock.ID, GlowInkFluidBlock::new);

    //===============
    // Items
    //===============
    public static final RegistryObject<Item> GLOW_BALL_ITEM = ITEMS.register(GlowBallBlock.ID, GlowBallItem::new);
    public static final RegistryObject<Item> GLOW_INK_BUCKET_ITEM = ITEMS.register(
        GlowInkBucketItem.ID, GlowInkBucketItem::new);
    public static final RegistryObject<Item> INK_GUN_ITEM = ITEMS.register(InkGunItem.ID, InkGunItem::new);

    //===============
    // Fluids
    //===============
    public static final RegistryObject<ForgeFlowingFluid> GLOW_INK_FLUID = FLUIDS.register(
        GlowInkFluid.Source.ID, GlowInkFluid.Source::new);
    public static final RegistryObject<ForgeFlowingFluid> GLOW_INK_FLUID_FLOWING = FLUIDS.register(
        GlowInkFluid.Flowing.ID, GlowInkFluid.Flowing::new);

    //===============
    // Entities
    //===============
    public static final RegistryObject<EntityType<GlowBallEntity>> GLOW_BALL_ENTITY = ENTITIES.register(GlowBallEntity.ID,
        () -> EntityType.Builder.<GlowBallEntity>of(GlowBallEntity::new, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .setTrackingRange(4)
                .setShouldReceiveVelocityUpdates(true)
                .build(GlowBallEntity.ID));

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GLOW_BALL_ENTITY.get(), ThrownItemRenderer::new);
    }
}
