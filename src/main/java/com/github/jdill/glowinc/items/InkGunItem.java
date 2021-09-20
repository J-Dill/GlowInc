package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

public class InkGunItem extends Item {

    public static final String ID = "ink_gun";

    private static final int INK_GUN_CAPACITY = 10 * FluidAttributes.BUCKET_VOLUME;
    private static final int INK_USE_AMOUNT = 250;

    public InkGunItem() {
        super(new Properties().maxStackSize(1).group(ItemGroup.COMBAT));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        if(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            Optional<FluidStack> fsCap = FluidUtil.getFluidContained(stack);
            fsCap.ifPresent(fs -> list.add(new StringTextComponent("A thing")));
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
                final CompoundNBT tag = stack.getChildTag("BigBuckets");
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
                final CompoundNBT tag = stack.getChildTag("BigBuckets");
                fixNBT(tag, fluidHandler, stack);
            }
            return fluidHandler.getFluid().getAmount();
        }
        return 0;
    }

    private void fixNBT(CompoundNBT tag, IFluidHandler fluidHandler, ItemStack stack) {
        final Fluid oldFluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(tag.getString("Fluid")));
        final int oldCapacity = tag.getInt("Capacity");
        final int oldFullness = tag.getInt("Fullness");

//        fluidHandler.setTankCapacity(oldCapacity * FluidAttributes.BUCKET_VOLUME);

        if (oldFluid == null) {
            stack.removeChildTag("BigBuckets");
            return;
        }

        final FluidStack fluidStack = new FluidStack(oldFluid, oldFullness);

        fluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
        fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        stack.removeChildTag("BigBuckets");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        Optional<FluidStack> fs = FluidUtil.getFluidContained(itemStack);
        if(fs.isPresent()) {
            FluidStack fluidStack = fs.get();
            if (fluidStack.getFluid() != null && fluidStack.getAmount() >= INK_USE_AMOUNT) {
                world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    GlowBallEntity glowBallEntity = new GlowBallEntity(player, world);
                    glowBallEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                    world.addEntity(glowBallEntity);
                }
                fluidStack.getOrCreateTag().put("Fluid", fluidStack.writeToNBT(new CompoundNBT()));
                fluidStack.shrink(INK_USE_AMOUNT);
            } else if (fluidStack.hasTag()) {
                CompoundNBT tag = fluidStack.getOrCreateTag();
                tag.remove("Fluid");
                if(tag.isEmpty()) {
                    fluidStack.setTag(null);
                }
            }
        }
        return ActionResult.func_233538_a_(itemStack, world.isRemote());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        if(!stack.isEmpty()) {
            return new FluidHandlerItemStack(stack, INK_GUN_CAPACITY);
        }
        return null;
    }

}
