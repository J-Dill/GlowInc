package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class GlowInkBucketItem extends BucketItem {

    public static final String ID = "glow_ink_bucket_item";

    public GlowInkBucketItem() {
        super(
                Registry.GLOW_INK_FLUID,
                new Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)
        );
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}
