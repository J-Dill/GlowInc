package com.github.jdill.glowinc;

import com.github.jdill.glowinc.client.ClientSetup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(GlowInc.MODID)
public class GlowInc {

    public static final String MODID = "glowinc";

    public GlowInc() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("glowinc-common.toml"));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.BLOCKS.register(modEventBus);
        Registry.ITEMS.register(modEventBus);
        Registry.ENTITIES.register(modEventBus);
        Registry.POTIONS.register(modEventBus);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initEarly);
    }

}
