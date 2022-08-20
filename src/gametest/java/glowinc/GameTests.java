package glowinc;

import com.github.jdill.glowinc.GlowInc;
import com.github.jdill.glowinc.Registry;
import com.github.jdill.glowinc.items.InkGunItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

import java.util.*;

@GameTestHolder(GlowInc.MODID)
public class GameTests {

    private static final Connection DUMMY_CONNECTION = new Connection(PacketFlow.CLIENTBOUND);
    static final BlockPos PLAYER_START = new BlockPos(3, 1, 0);
    static final BlockPos SPLAT_POS = new BlockPos(3, 2, 5);

    @PrefixGameTestTemplate(value = false)
    @GameTest(template = "gun_platform")
    public static void testGunQuickFill(GameTestHelper helper) {
        // Make survival player.
        ServerPlayer player = makeMockPlayer(helper.getLevel(), "quick-fill");
        Inventory playerInventory = player.getInventory();
        player.setGameMode(GameType.SURVIVAL);
        player.moveTo(helper.absolutePos(PLAYER_START), 0, 0);

        // Add empty Ink Gun to player's inventory.
        ItemStack defaultInkGun = Registry.INK_GUN_ITEM.get().getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, defaultInkGun);
        player.addItem(Registry.PURE_GLOW_BOTTLE.get().getDefaultInstance());

        // Try to do the quick fill.
        Optional<ItemStack> inkGun = getInkGun(playerInventory);
        if (inkGun.isEmpty()) {
            helper.fail("Ink Gun was not added to player inventory.");
        }
        player.setShiftKeyDown(true);
        player.gameMode.useItem(player, helper.getLevel(), inkGun.get(), InteractionHand.MAIN_HAND);
        player.setShiftKeyDown(false);

        // Look for empty bottle and absence of Pure Glow Bottle.
        HashSet<Item> bottle = new HashSet<>(List.of(Registry.PURE_GLOW_BOTTLE.get()));
        HashSet<Item> emptyBottle = new HashSet<>(List.of(Items.GLASS_BOTTLE));
        if (playerInventory.hasAnyOf(bottle) ) {
            helper.fail("Pure Glow Bottle was not consumed.");
        } else if (!playerInventory.hasAnyOf(emptyBottle)) {
            helper.fail("An empty bottle should be in the player's inventory.");
        }

        // Check that Ink Gun was filled (don't care how much).
        inkGun = getInkGun(playerInventory);
        if (inkGun.isEmpty() || inkGun.get().getDamageValue() >= InkGunItem.INK_GUN_CAPACITY) {
             helper.fail("Ink Gun was not filled.");
        }

        helper.succeed();
    }

    @PrefixGameTestTemplate(value = false)
    @GameTest(template = "gun_platform_wall")
    public static void testGunShoot(GameTestHelper helper) {
        // Make survival player.
        ServerPlayer player = makeMockPlayer(helper.getLevel(), "gun-shoot");
        Inventory playerInventory = player.getInventory();
        player.setGameMode(GameType.SURVIVAL);
        player.moveTo(helper.absolutePos(PLAYER_START), 0, 0);

        // Add full Ink Gun to player's inventory.
        ItemStack defaultInkGun = Registry.INK_GUN_ITEM.get().getDefaultInstance();
        defaultInkGun.setDamageValue(0);
        player.setItemInHand(InteractionHand.MAIN_HAND, defaultInkGun);
        player.addItem(Registry.PURE_GLOW_BOTTLE.get().getDefaultInstance());

        // Try to shoot.
        Optional<ItemStack> inkGun = getInkGun(playerInventory);
        if (inkGun.isEmpty()) {
            helper.fail("Ink Gun was not added to player inventory.");
        }
        player.gameMode.useItem(player, helper.getLevel(), inkGun.get(), InteractionHand.MAIN_HAND);

        // Check that Ink Gun is not full.
        inkGun = getInkGun(playerInventory);
        if (inkGun.isEmpty() || inkGun.get().getDamageValue() == 0) {
            helper.fail("Ink Gun was not emptied on use.");
        }

        // Check for ink splat.
        helper.succeedWhenBlockPresent(Registry.GLOW_BALL_BLOCK.get(), SPLAT_POS);
    }

    private static Optional<ItemStack> getInkGun(Inventory playerInventory) {
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item.getItem() instanceof InkGunItem) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    private static ServerPlayer makeMockPlayer(Level level, String name) {
        ServerPlayer player = new ServerPlayer(Objects.requireNonNull(level.getServer()), (ServerLevel) level,
                new GameProfile(UUID.randomUUID(), "test-mock-player-" + name), null
        );
        player.connection = new ServerGamePacketListenerImpl(level.getServer(), DUMMY_CONNECTION, player);
        return player;
    }

}
