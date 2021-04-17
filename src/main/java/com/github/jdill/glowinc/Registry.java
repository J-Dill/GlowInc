package com.github.jdill.glowinc;

import com.github.jdill.glowinc.blocks.GlowBallBlock;
import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import com.github.jdill.glowinc.items.GlowBallItem;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GlowInc.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GlowInc.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, GlowInc.MODID);

    //===============
    // Blocks
    //===============
    public static final RegistryObject<Block> GLOW_BALL_BLOCK = BLOCKS.register(GlowBallBlock.ID, GlowBallBlock::new);

    //===============
    // Items
    //===============
    public static final RegistryObject<Item> GLOW_BALL_ITEM = ITEMS.register(GlowBallBlock.ID, GlowBallItem::new);

    //===============
    // Entities
    //===============
    public static final RegistryObject<EntityType<GlowBallEntity>> SAP_ENTITY = ENTITIES.register(GlowBallEntity.ID,
        () -> EntityType.Builder.<GlowBallEntity>create(GlowBallEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .trackingRange(4)
            .setShouldReceiveVelocityUpdates(true)
            .build(GlowBallEntity.ID));
}
