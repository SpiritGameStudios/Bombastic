package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BombasticEnchantmentComponentTypeRegistrar implements MinecraftRegistrar<ComponentType<?>> {
    public static final ComponentType<EnchantmentValueEffect> JUGGLING_BALL_BOUNCE = ComponentType.<EnchantmentValueEffect>builder()
            .codec(EnchantmentValueEffect.CODEC)
            .build();

    public static final ComponentType<EnchantmentValueEffect> PARTY_POPPER_EXPLOSION = ComponentType.<EnchantmentValueEffect>builder()
            .codec(EnchantmentValueEffect.CODEC)
            .build();


    @Override
    public Registry<ComponentType<?>> getRegistry() { return Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE; }

    @Override
    public Class<ComponentType<?>> getObjectType() {
        return Registrar.fixGenerics(ComponentType.class);
    }
}
