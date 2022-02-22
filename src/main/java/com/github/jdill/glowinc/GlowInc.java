package com.github.jdill.glowinc;

import com.github.jdill.glowinc.client.ClientSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GlowInc.MODID)
public class GlowInc {

    public static final String MODID = "glowinc";

    public GlowInc() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.BLOCKS.register(modEventBus);
        Registry.ITEMS.register(modEventBus);
        Registry.ENTITIES.register(modEventBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initEarly);
    }

}
