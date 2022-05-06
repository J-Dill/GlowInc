package com.github.jdill.glowinc.client.screens;

import com.github.jdill.glowinc.inventory.SqueezerMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class SqueezerScreen extends AbstractContainerScreen<SqueezerMenu> {

    public SqueezerScreen(SqueezerMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(@Nonnull PoseStack stack, float p_97788_, int p_97789_, int p_97790_) {
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(stack, relX - 23, relY, 0, 0, this.imageWidth + 23, this.imageHeight);
    }
}
