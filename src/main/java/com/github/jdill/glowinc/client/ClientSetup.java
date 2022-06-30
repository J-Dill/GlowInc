package com.github.jdill.glowinc.client;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.items.InkGunItem;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class ClientSetup {

    public static void initEarly() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Registry.GLOW_BALL_BLOCK.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void removeDurabilityText(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof InkGunItem) {
            List<Component> toolTip = event.getToolTip();
            for (int i = 0; i < toolTip.size(); i++) {
                Component component = toolTip.get(i);
                ComponentContents contents = component.getContents();
                if (contents instanceof TranslatableContents translatableContents) {
                    if ("item.durability".equals(translatableContents.getKey())) {
                        toolTip.remove(i);
                    }
                }
            }
        }
    }

}
