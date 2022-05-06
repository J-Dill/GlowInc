package com.github.jdill.glowinc.items;

import com.github.jdill.glowinc.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class SqueezerItem extends BlockItem {

    public static final String ID = "squeezer";

    public SqueezerItem() {
        super(Registry.SQUEEZER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    }

}
