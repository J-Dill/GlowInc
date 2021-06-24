package com.github.jdill.glowincfabric.client;

import com.github.jdill.glowincfabric.GlowincFabric;
import com.github.jdill.glowincfabric.entity.EntitySpawnPacket;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class GlowincFabricClient implements ClientModInitializer {

    public static final Identifier PACKET_ID = new Identifier(GlowincFabric.MOD_ID, "spawn_packet");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(GlowincFabric.GLOW_BALL_ENTITY,
            FlyingItemEntityRenderer::new);
        receiveEntityPacket();
    }

    public void receiveEntityPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID, (ctx, clientHandler, byteBuf, sender) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.executeTask(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }

}
