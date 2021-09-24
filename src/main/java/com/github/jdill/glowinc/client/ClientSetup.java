package com.github.jdill.glowinc.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSetup {

    public static void initEarly() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
//        RenderingRegistry.registerEntityRenderingHandler(Registry.GLOW_BALL_ENTITY.get(),
//            manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
    }

}
