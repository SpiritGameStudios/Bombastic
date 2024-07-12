package dev.callmeecho.bombastic.main.network;

import com.mojang.datafixers.util.Pair;
import dev.callmeecho.bombastic.main.Bombastic;
import dev.callmeecho.bombastic.main.registry.BombasticParticleRegistrar;
import dev.callmeecho.cabinetapi.CabinetAPI;
import dev.callmeecho.cabinetapi.config.network.ConfigSyncPacket;
import dev.callmeecho.cabinetapi.config.network.ConfigSyncPayload;
import dev.callmeecho.cabinetapi.network.CabinetPacketCodecs;
import dev.callmeecho.cabinetapi.network.CabinetS2CPacket;
import dev.callmeecho.cabinetapi.util.Singleton;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.List;

public class PartyPopperS2CPacket implements CabinetS2CPacket<PartyPopperS2CPacket.PartyPopperPayload> {
    public static final CustomPayload.Id<PartyPopperPayload> ID = new CustomPayload.Id<>(Identifier.of(Bombastic.MODID, "party_popper"));
    public static final PacketCodec<ByteBuf, PartyPopperPayload> CODEC =
            PacketCodec.tuple(
                    CabinetPacketCodecs.pair(
                            PacketCodecs.VECTOR3F,
                            PacketCodecs.VECTOR3F),
                    PartyPopperPayload::posRot,
                    PartyPopperPayload::new
            );

    public static final Singleton<PartyPopperS2CPacket> SINGLETON = new Singleton<>(PartyPopperS2CPacket.class);

    @Override
    public void receive(PartyPopperPayload payload, ClientPlayNetworking.Context context) {
        Random random = context.player().getRandom();
        Vec3d direction = new Vec3d(payload.posRot().getSecond());
        Vector3f pos = payload.posRot().getFirst();
        for (int i = 0; i < 250; i++) {
            Vec3d velocity = new Vec3d(
                    random.nextGaussian() * 0.15,
                    random.nextGaussian() * 0.15,
                    random.nextGaussian() * 0.15
            );
            velocity = velocity.add(direction.multiply(0.75F));

            context.player().getWorld().addParticle(
                    BombasticParticleRegistrar.CONFETTI,
                    pos.x(),
                    pos.y() - 0.25F,
                    pos.z(),
                    velocity.getX(),
                    velocity.getY(),
                    velocity.getZ()
            );
        }
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
    @Override
    public <B extends PacketByteBuf> PacketCodec<? super B, ? extends CustomPayload> getCodec() { return CODEC; }

    public record PartyPopperPayload(Pair<Vector3f, Vector3f> posRot) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return PartyPopperS2CPacket.ID;
        }
    }

}
