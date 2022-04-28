package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import com.github.jdill.glowinc.fluids.InkGunFluidHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class InkGunItem extends Item {

    public static final String ID = "ink_gun";

    private static final int INK_GUN_CAPACITY = 10 * FluidAttributes.BUCKET_VOLUME;
    private static final int INK_USE_AMOUNT = 100;
    private static final SoundEvent SHOOT_SOUND = SoundEvents.SLIME_ATTACK;

    public InkGunItem() {
        super(new Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT).setNoRepair());
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable Level level, @Nonnull List<Component> textList, @Nonnull TooltipFlag flag) {
        if(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            Optional<FluidStack> fsCap = FluidUtil.getFluidContained(itemStack);
            AtomicInteger amount = new AtomicInteger();
            fsCap.ifPresent(fs -> amount.set(fs.getAmount()));
            String amountMsg = "Ink: " + amount + "/" + INK_GUN_CAPACITY;
            textList.add(new TextComponent(amountMsg));
        }
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        Optional<FluidStack> fluidContained = FluidUtil.getFluidContained(stack);
        if (fluidContained.isPresent()) {
            int currentAmount = fluidContained.get().getAmount();
            return Math.min(13 * currentAmount / INK_GUN_CAPACITY, 13);
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        Optional<FluidStack> fluidContained = FluidUtil.getFluidContained(stack);
        if (fluidContained.isPresent()) {
            int currentAmount = fluidContained.get().getAmount();
            return Mth.hsvToRgb(Math.max(0.0F, (float) currentAmount / (float) INK_GUN_CAPACITY) / 3.0F, 1.0F, 1.0F);
        }
        return super.getBarColor(stack);
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> subItems) {
        // Creates an empty version of the Ink Gun
        super.fillItemCategory(tab, subItems);
        if (!allowdedIn(tab)) {
            return;
        }

        // Creates a full version of the Ink Gun
        FluidStack fluidStack = new FluidStack(Registry.GLOW_INK_FLUID.get(), INK_GUN_CAPACITY);
        ItemStack itemStack = new ItemStack(this);
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            LazyOptional<IFluidHandlerItem> maybeHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            maybeHandler.ifPresent((fluidHandler) -> {
                fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                ItemStack filledStack = fluidHandler.getContainer();
                subItems.add(filledStack);
            });
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Optional<FluidStack> fs = FluidUtil.getFluidContained(itemStack);
        if(fs.isPresent()) {
            FluidStack fluidStack = fs.get();
            if (fluidStack.getFluid() != null && fluidStack.getAmount() >= INK_USE_AMOUNT) {
                // Play shoot sound
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SHOOT_SOUND,
                        SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
                );

                // Spawn entity
                if (!level.isClientSide()) {
                    GlowBallEntity glowBallEntity = new GlowBallEntity(player, level);
                    glowBallEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
                    level.addFreshEntity(glowBallEntity);
                }

                // Drain ink from Ink Gun
                LazyOptional<IFluidHandlerItem> maybeHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
                maybeHandler.ifPresent((fluidHandler) -> fluidHandler.drain(INK_USE_AMOUNT, IFluidHandler.FluidAction.EXECUTE));
                itemStack.hurt(INK_USE_AMOUNT, new Random(), null);
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
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new InkGunFluidHandler(stack, INK_GUN_CAPACITY);
    }
}
