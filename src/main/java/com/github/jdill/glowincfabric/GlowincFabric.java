package com.github.jdill.glowincfabric;

import com.github.jdill.glowincfabric.blocks.GlowBallBlock;
import com.github.jdill.glowincfabric.entity.projectile.GlowBallEntity;
import com.github.jdill.glowincfabric.items.GlowBallItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class GlowincFabric implements ModInitializer {

    public static final String MOD_ID = "glowinc-fabric";

    public static final GlowBallBlock GLOW_BALL_BLOCK = new GlowBallBlock();

    public static final GlowBallItem GLOW_BALL_ITEM = new GlowBallItem();

    public static final EntityType<GlowBallEntity> GLOW_BALL_ENTITY = Registry.register(
        Registry.ENTITY_TYPE,
        GlowBallEntity.ID,
        FabricEntityTypeBuilder.<GlowBallEntity>create(SpawnGroup.MISC, GlowBallEntity::new)
            .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
            .build()
    );

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, GlowBallBlock.ID, GLOW_BALL_BLOCK);
        Registry.register(Registry.ITEM, GlowBallBlock.ID, GLOW_BALL_ITEM);
    }
}
