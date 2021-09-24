package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class InkGunItem extends Item {

    public static final String ID = "ink_gun";

    private static final int INK_GUN_CAPACITY = 10 * FluidAttributes.BUCKET_VOLUME;
    private static final int INK_USE_AMOUNT = 250;

    public InkGunItem() {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> textList, TooltipFlag flag) {
        if(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            Optional<FluidStack> fsCap = FluidUtil.getFluidContained(itemStack);
            fsCap.ifPresent(fs -> textList.add(new TextComponent("A thing")));
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 0;
    }

    public int getCapacity(ItemStack stack) {
        final LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (cap.isPresent()) {
            final IFluidHandler fluidHandler = cap.orElseThrow(NullPointerException::new);
            if (stack.hasTag() && stack.getTag().contains("BigBuckets")) {
                // Handling for old-style NBT
                final CompoundTag tag = stack.getTagElement("BigBuckets");
                fixNBT(tag, fluidHandler, stack);
            }
            return fluidHandler.getTankCapacity(0);
        }
        return 0;
    }

    public int getFullness(ItemStack stack) {
        final LazyOptional<IFluidHandlerItem> cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);

        if (cap.isPresent()) {
            final FluidHandlerItemStack fluidHandler = (FluidHandlerItemStack) cap.orElseThrow(NullPointerException::new);
            if (stack.hasTag() && stack.getTag().contains("BigBuckets")) {
                // Handling for old-style NBT
                final CompoundTag tag = stack.getTagElement("BigBuckets");
                fixNBT(tag, fluidHandler, stack);
            }
            return fluidHandler.getFluid().getAmount();
        }
        return 0;
    }

    private void fixNBT(CompoundTag tag, IFluidHandler fluidHandler, ItemStack stack) {
        final Fluid oldFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString("Fluid")));
        final int oldCapacity = tag.getInt("Capacity");
        final int oldFullness = tag.getInt("Fullness");

//        fluidHandler.setTankCapacity(oldCapacity * FluidAttributes.BUCKET_VOLUME);

        if (oldFluid == null) {
            stack.removeTagKey("BigBuckets");
            return;
        }

        final FluidStack fluidStack = new FluidStack(oldFluid, oldFullness);

        fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        stack.removeTagKey("BigBuckets");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Optional<FluidStack> fs = FluidUtil.getFluidContained(itemStack);
        if(fs.isPresent()) {
            FluidStack fluidStack = fs.get();
            if (fluidStack.getFluid() != null && fluidStack.getAmount() >= INK_USE_AMOUNT) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
                        SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
                );
                if (!level.isClientSide()) {
                    GlowBallEntity glowBallEntity = new GlowBallEntity(player, level);
                    glowBallEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                    level.addFreshEntity(glowBallEntity);
                }
                fluidStack.getOrCreateTag().put("Fluid", fluidStack.writeToNBT(new CompoundTag()));
                fluidStack.shrink(INK_USE_AMOUNT);
            } else if (fluidStack.hasTag()) {
                CompoundTag tag = fluidStack.getOrCreateTag();
                tag.remove("Fluid");
                if(tag.isEmpty()) {
                    fluidStack.setTag(null);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        if(!stack.isEmpty()) {
            return new FluidHandlerItemStack(stack, INK_GUN_CAPACITY);
        }
        return null;
    }

}
