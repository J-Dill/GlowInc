package com.github.jdill.glowinc.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PureGlowBottleItem extends BottleItem {

    public static final String ID = "pure_glow_bottle";

    public PureGlowBottleItem() {
        super((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_BREWING).craftRemainder(Items.GLASS_BOTTLE));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(new TextComponent("Deprecated: Replaced by Potion of Pure Glow").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(itemStack, level, components, flag);
    }
}
