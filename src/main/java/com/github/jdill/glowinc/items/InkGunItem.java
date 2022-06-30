package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.entity.projectile.GlowBallEntity;
import com.github.jdill.glowinc.fluids.InkGunFluidHandler;
import com.github.jdill.glowinc.recipes.InkGunRefillRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InkGunItem extends Item {

    public static final String ID = "ink_gun";

    private static final int INK_GUN_CAPACITY = 6400;
    private static final int INK_USE_AMOUNT = 100;
    private static final SoundEvent SHOOT_SOUND = SoundEvents.SLIME_ATTACK;

    public InkGunItem() {
        super(new Properties()
                .stacksTo(1)
                .tab(CreativeModeTab.TAB_TOOLS)
                .setNoRepair()
                .defaultDurability(INK_GUN_CAPACITY));
    }

    @Nonnull
    @Override
    public ItemStack getDefaultInstance() {
        NonNullList<ItemStack> guns = NonNullList.create();
        addInkGunWithAmount(guns, 0);
        return guns.get(0);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable Level level, @Nonnull List<Component> textList, @Nonnull TooltipFlag flag) {
        if(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            Optional<FluidStack> fsCap = FluidUtil.getFluidContained(itemStack);
            AtomicInteger amount = new AtomicInteger();
            fsCap.ifPresent(fs -> amount.set(fs.getAmount()));
            String amountMsg = "Ink: " + amount + "/" + INK_GUN_CAPACITY;
            textList.add(Component.literal(amountMsg));
        }
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 20;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return true;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return true;
    }



    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.allowedIn(tab)) {
            // Creates an empty version of the Ink Gun.
            addInkGunWithAmount(subItems, 0);

            // Creates a full version of the Ink Gun.
            addInkGunWithAmount(subItems, INK_GUN_CAPACITY);
        }
    }

    /**
     * Add an Ink Gun with given amount of fluid in tank to the provided list.
     * @param itemList The list to add the Ink Gun to.
     * @param amount Amount of fluid to add.
     */
    private void addInkGunWithAmount(NonNullList<ItemStack> itemList, int amount) {
        FluidStack fluidStack = new FluidStack(Registry.GLOW_INK_FLUID.get(), amount);
        ItemStack itemStack = new ItemStack(this);
        if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY != null) {
            LazyOptional<IFluidHandlerItem> maybeHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY);
            maybeHandler.ifPresent((fluidHandler) -> {
                fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                ItemStack filledStack = fluidHandler.getContainer();
                itemList.add(filledStack);
            });
        }
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return super.isDamageable(stack);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return super.isDamaged(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return INK_GUN_CAPACITY;
    }

    @Override
    public int getDamage(ItemStack stack) {
        Optional<FluidStack> fluidStack = getFluidContained(stack);
        return INK_GUN_CAPACITY - fluidStack.map(FluidStack::getAmount).orElse(0);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        LazyOptional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(stack);
        fluidHandler.ifPresent(
                (e) -> e.fill(new FluidStack(Registry.GLOW_INK_FLUID.get(), INK_GUN_CAPACITY - damage), IFluidHandler.FluidAction.EXECUTE)
        );
    }

    public static Optional<FluidStack> getFluidContained(@NotNull ItemStack container) {
        if (!container.isEmpty()) {
            Optional<FluidStack> fluidContained = FluidUtil.getFluidHandler(container)
                    .map(handler -> handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE));
            if (fluidContained.isPresent() && !fluidContained.get().isEmpty()) {
                return fluidContained;
            }
        }
        return Optional.empty();
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack inkGun = player.getItemInHand(hand);
        Optional<FluidStack> fs = FluidUtil.getFluidContained(inkGun);
        LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(inkGun);
        if (!player.isUsingItem()) {
            if (player.isShiftKeyDown() && inkGun.isDamaged()) {
                // Player can refill Ink Gun by shift-clicking it with Pure Glow Bottle in their inventory.
                Inventory inventory = player.getInventory();
                InkGunRefillRecipe refillRecipe = InkGunRefillRecipe.getInstance(level);
                Ingredient ingredient = refillRecipe.getIngredient().orElse(Ingredient.EMPTY);
                Optional<ItemStack> firstRefillItem = Arrays.stream(ingredient.getItems())
                        .filter(itemStack -> !itemStack.isEmpty())
                        .filter(itemStack -> 0 <= inventory.findSlotMatchingItem(itemStack))
                        .findFirst();
                if (firstRefillItem.isPresent()) {
                    int refillItemSlot = inventory.findSlotMatchingItem(firstRefillItem.get());
                    ItemStack refillItemStack = inventory.getItem(refillItemSlot);
                    CraftingContainer container = getInkGunRefillContainer(inkGun, refillItemStack);
                    ItemStack filledGun = refillRecipe.assemble(container);
                    player.setItemInHand(hand, filledGun);
                    NonNullList<ItemStack> remainingItems = refillRecipe.getRemainingItems(container);
                    if (!remainingItems.isEmpty()) {
                        for (ItemStack remainingItem : remainingItems) {
                            if (!remainingItem.isEmpty()) {
                                inventory.add(remainingItem);
                            }
                        }

                    }
                    refillItemStack.shrink(1);
                }
            } else if (fs.isPresent()) {
                // If there is ink, shoot the gun.
                if (!player.getCooldowns().isOnCooldown(inkGun.getItem())) {
                    FluidStack fluidStack = fs.get();
                    if (fluidStack.getFluid() != null && fluidStack.getAmount() >= INK_USE_AMOUNT) {
                        // Play shoot sound
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SHOOT_SOUND,
                                SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
                        );

                        // Spawn Glow Ball
                        if (!level.isClientSide()) {
                            GlowBallEntity glowBallEntity = new GlowBallEntity(player, level);
                            glowBallEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);
                            level.addFreshEntity(glowBallEntity);
                        }

                        if (!player.isCreative()) {
                            // Drain ink from Ink Gun
                            handler.ifPresent((fluidHandler) -> fluidHandler.drain(INK_USE_AMOUNT, IFluidHandler.FluidAction.EXECUTE));
                        }
                        player.getCooldowns().addCooldown(this, 10);
                    } else if (fluidStack.hasTag()) {
                        CompoundTag tag = fluidStack.getOrCreateTag();
                        tag.remove("Fluid");
                        if (tag.isEmpty()) {
                            fluidStack.setTag(null);
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.pass(inkGun);
    }

    /**
     * Creating a fake crafting container to simulate filling the Ink Gun with one Pure Ink Bottle
     * from the player's inventory.
     * @param inkGun This item.
     * @param bottleStack A stack of Pure Glow Bottles.
     * @return A fake crafting container to fill the Ink Gun.
     */
    @Nonnull
    private CraftingContainer getInkGunRefillContainer(ItemStack inkGun, ItemStack bottleStack) {
        CraftingContainer container = new CraftingContainer(new AbstractContainerMenu(null, -1) {
            public ItemStack quickMoveStack(Player player, int slot) {
                return ItemStack.EMPTY;
            }

            public boolean stillValid(Player player) {
                return false;
            }
        }, 2, 1);
        container.setItem(0, inkGun);
        container.setItem(1, bottleStack);
        return container;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity user, int count) {

    }

    @Override
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new InkGunFluidHandler(stack, INK_GUN_CAPACITY);
    }
}
