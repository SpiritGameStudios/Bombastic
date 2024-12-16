package dev.spiritstudios.bombastic.main.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;

public class BombasticDataComponentTypes {
    public static final ComponentType<Boolean> PINNED = ComponentType.<Boolean>builder()
            .codec(Codec.BOOL)
            .packetCodec(PacketCodecs.BOOL)
            .cache()
            .build();

    public static final ComponentType<List<ItemStack>> TRIGGERS = ComponentType.<List<ItemStack>>builder()
            .codec(ItemStack.CODEC.listOf())
            .packetCodec(ItemStack.LIST_PACKET_CODEC)
            .cache()
            .build();
}
