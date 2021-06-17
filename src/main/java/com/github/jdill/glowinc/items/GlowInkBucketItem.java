package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.Registry;
import javax.annotation.Nullable;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class GlowInkBucketItem extends BucketItem {

    public static final String ID = "glow_ink_bucket_item";

    public GlowInkBucketItem() {
        super(
                Registry.GLOW_INK_FLUID,
                new Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.MISC)
        );
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidBucketWrapper(stack);
    }
}
