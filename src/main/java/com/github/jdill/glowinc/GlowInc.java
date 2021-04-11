package com.github.jdill.glowinc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(GlowInc.MODID)
public class GlowInc {

    public static final String MODID = "glowinc";

    private static final ResourceLocation inkSacLoc = new ResourceLocation("ink_sac");
    private static final IFormattableTextComponent text =
        new StringTextComponent("Right block on block to create Glow Ink Blot")
        .mergeStyle(TextFormatting.GOLD, TextFormatting.ITALIC);

    public GlowInc() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.BLOCKS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(Registry.GLOW_INK_BLOCK.get(), RenderType.getTranslucent());
    }

    @SubscribeEvent
    public void rightClickBlockEvent(RightClickBlock event) {
        if (isInkSac(event.getItemStack())) {
            BlockPos up = event.getPos().up();
            World world = event.getWorld();
            BlockState blockStateUp = world.getBlockState(up);
            if (blockStateUp.isAir()) {
                Block diamondBlock = Registry.GLOW_INK_BLOCK.get();
                world.setBlockState(up, diamondBlock.getDefaultState());
            }
        }
    }

    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        if (isInkSac(event.getItemStack())) {
            event.getToolTip().add(text);
        }
    }

    private static boolean isInkSac(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        return name != null && name.equals(inkSacLoc);
    }

}
