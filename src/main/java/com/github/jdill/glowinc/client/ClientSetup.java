package com.github.jdill.glowinc.client;

import com.github.jdill.glowinc.Registry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSetup {

    public static void initEarly() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registry.GLOW_BALL_BLOCK.get(), RenderType.cutout());
    }

}
