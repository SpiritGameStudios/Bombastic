package dev.spiritstudios.bombastic.main.registry;

import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;

public class BombasticEnchantmentComponentTypes {
    public static final ComponentType<EnchantmentValueEffect> JUGGLING_BALL_BOUNCE = ComponentType.<EnchantmentValueEffect>builder()
            .codec(EnchantmentValueEffect.CODEC)
            .build();

    public static final ComponentType<EnchantmentValueEffect> PARTY_POPPER_EXPLOSION = ComponentType.<EnchantmentValueEffect>builder()
            .codec(EnchantmentValueEffect.CODEC)
            .build();
}
