package com.github.jdill.glowinc.data;

import com.github.jdill.glowinc.GlowInc;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = GlowInc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Generator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if(event.includeServer()) {
            registerServerProviders(event.getGenerator());
        }
    }

    private static void registerServerProviders(DataGenerator generator) {
        generator.addProvider(new GeneratorRecipes(generator));
    }

}
