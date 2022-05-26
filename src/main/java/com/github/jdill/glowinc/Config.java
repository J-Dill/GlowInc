package com.github.jdill.glowinc;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class Config {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue GLOW_BALL_BLOCK_PERSISTENT;
    public static ForgeConfigSpec.IntValue GLOW_BALL_BLOCK_MINUTES;

    static {
        COMMON_BUILDER.comment("Glow Ball").push("glow_ball");

        GLOW_BALL_BLOCK_PERSISTENT = COMMON_BUILDER.comment("Whether the Glow Ball splat will disappear, " +
                        "regardless of the glowSplatMinutes.")
                .define("glowSplatPersistent", false);

        GLOW_BALL_BLOCK_MINUTES = COMMON_BUILDER.comment("Number of minutes the Glow Ball splat will last.",
                        "Ignored if glowSplatPersistent is to set to true.")
            .defineInRange("glowSplatMinutes", 10, 1, 120);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }

}
