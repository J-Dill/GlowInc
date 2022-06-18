package com.github.jdill.glowinc.client;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.client.screens.SqueezerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registry.GLOW_BALL_BLOCK.get(), RenderType.cutout());

        MenuScreens.register(Registry.SQUEEZER_MENU.get(), SqueezerScreen::new);
    }

}
