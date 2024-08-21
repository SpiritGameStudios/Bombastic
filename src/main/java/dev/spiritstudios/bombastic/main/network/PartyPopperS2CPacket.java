package dev.spiritstudios.bombastic.main.network;

import com.mojang.datafixers.util.Pair;
import dev.spiritstudios.bombastic.main.Bombastic;
import dev.spiritstudios.bombastic.main.registry.BombasticParticleRegistrar;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

public class PartyPopperS2CPacket {
    public static final CustomPayload.Id<PartyPopperPayload> ID = new CustomPayload.Id<>(Identifier.of(Bombastic.MODID, "party_popper"));
    public static final PacketCodec<ByteBuf, PartyPopperPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodec.tuple(
                            PacketCodecs.VECTOR3F,
                            Pair::getFirst,
                            PacketCodecs.VECTOR3F,
                            Pair::getSecond,
                            Pair::of
                    ),
                    PartyPopperPayload::posRot,
                    PartyPopperPayload::new
            );

    public static void receive(PartyPopperPayload payload, ClientPlayNetworking.Context context) {
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

    public static void send(PartyPopperPayload payload, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, payload);
    }

    public record PartyPopperPayload(Pair<Vector3f, Vector3f> posRot) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() {
            return PartyPopperS2CPacket.ID;
        }
    }
}
