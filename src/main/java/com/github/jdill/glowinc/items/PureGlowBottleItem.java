package com.github.jdill.glowinc.items;

import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PureGlowBottleItem extends BottleItem {

    public static final String ID = "pure_glow_bottle";

    public PureGlowBottleItem() {
        super((new Item.Properties()).stacksTo(16)
                .tab(CreativeModeTab.TAB_BREWING)
                .craftRemainder(Items.GLASS_BOTTLE));
    }

}
