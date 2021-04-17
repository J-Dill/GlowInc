package com.github.jdill.glowinc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
        Registry.ITEMS.register(modEventBus);
        Registry.ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }



//    @SubscribeEvent
//    public void rightClickBlockEvent(RightClickBlock event) {
//        ItemStack itemStack = event.getItemStack();
//        if (isInkSac(itemStack)) {
//            BlockPos up = event.getPos().up();
//            World world = event.getWorld();
//            BlockState blockStateUp = world.getBlockState(up);
//            if (blockStateUp.isAir()) {
//                Block diamondBlock = Registry.GLOW_INK_BLOCK.get();
//                world.setBlockState(up, diamondBlock.getDefaultState());
//                PlayerEntity player = event.getPlayer();
//                LazyOptional<IItemHandler> capability = player
//                    .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
//                capability.ifPresent(cap ->
//                    cap.extractItem(player.inventory.currentItem, 1, false)
//                );
//            }
//        }
//    }

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
