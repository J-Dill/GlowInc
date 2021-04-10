package com.github.jdill.glowinc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(GlowInc.MODID)
public class GlowInc {

    public static final String MODID = "glowinc";

    private static final ResourceLocation inkSacLoc = new ResourceLocation("ink_sac");
    private static final IFormattableTextComponent text =
        new StringTextComponent("Right block on block to create Glow Ink Blot")
        .mergeStyle(TextFormatting.YELLOW, TextFormatting.ITALIC);

    public GlowInc() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void rightClickBlockEvent(RightClickBlock event) {
        if (isInkSac(event.getItemStack())) {
            BlockPos up = event.getPos().up();
            World world = event.getWorld();
            BlockState blockStateUp = world.getBlockState(up);
            if (blockStateUp.isAir()) {
                Block diamondBlock = Blocks.GLOWSTONE;
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
