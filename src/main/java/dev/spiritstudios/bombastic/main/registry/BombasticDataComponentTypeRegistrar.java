package dev.spiritstudios.bombastic.main.registry;

import com.mojang.serialization.Codec;
import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public class BombasticDataComponentTypeRegistrar implements MinecraftRegistrar<ComponentType<?>> {
    public static final ComponentType<Boolean> PINNED = ComponentType.<Boolean>builder()
            .codec(Codec.BOOL)
            .packetCodec(PacketCodecs.BOOL)
            .build();

    public static final ComponentType<List<ItemStack>> TRIGGERS = ComponentType.<List<ItemStack>>builder()
            .codec(ItemStack.CODEC.listOf())
            .packetCodec(ItemStack.LIST_PACKET_CODEC)
            .build();

    @Override
    public Registry<ComponentType<?>> getRegistry() { return Registries.DATA_COMPONENT_TYPE; }
}
