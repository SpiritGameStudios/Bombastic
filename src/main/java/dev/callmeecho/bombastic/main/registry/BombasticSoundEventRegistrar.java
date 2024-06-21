package dev.callmeecho.bombastic.main.registry;

import dev.callmeecho.cabinetapi.registry.Registrar;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BombasticSoundEventRegistrar implements Registrar<SoundEvent> {
    public static final SoundEvent CLOWN_BOOTS = SoundEvent.of(Identifier.of("bombastic", "clown_boots"));
    public static final SoundEvent PARRY = SoundEvent.of(Identifier.of("bombastic", "parry"));


    @Override
    public Registry<SoundEvent> getRegistry() { return Registries.SOUND_EVENT; }
}
