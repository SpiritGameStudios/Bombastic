package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BombasticParticleRegistrar implements MinecraftRegistrar<ParticleType<?>> {
    public static final SimpleParticleType CONFETTI = FabricParticleTypes.simple();
    public static final SimpleParticleType FIRECRACKER_FLASH = FabricParticleTypes.simple();

    public static final SimpleParticleType NULL = FabricParticleTypes.simple();


    @Override
    public Registry<ParticleType<?>> getRegistry() { return Registries.PARTICLE_TYPE; }
}
