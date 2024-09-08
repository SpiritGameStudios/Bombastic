package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.specter.api.registry.registration.MinecraftRegistrar;
import dev.spiritstudios.specter.api.registry.registration.SoundEventRegistrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BombasticSoundEventRegistrar implements SoundEventRegistrar {
    public static final SoundEvent CLOWN_BOOTS = SoundEvent.of(Identifier.of("bombastic", "clown_boots"));
    public static final SoundEvent PARTY_POPPER = SoundEvent.of(Identifier.of("bombastic", "party_popper"));
    public static final SoundEvent JUGGLING_BALL = SoundEvent.of(Identifier.of("bombastic", "juggling_ball"));
    public static final SoundEvent PARRY = SoundEvent.of(Identifier.of("bombastic", "parry"));
}
