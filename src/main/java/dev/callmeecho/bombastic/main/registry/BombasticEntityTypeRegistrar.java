package dev.callmeecho.bombastic.main.registry;

import dev.callmeecho.bombastic.main.entity.JugglingBallEntity;
import dev.callmeecho.bombastic.main.entity.PipeBombEntity;
import dev.callmeecho.cabinetapi.registry.Registrar;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

@SuppressWarnings("unused")
public class BombasticEntityTypeRegistrar implements Registrar<EntityType<?>> {
    public static final EntityType<PipeBombEntity> PIPE_BOMB = EntityType.Builder.create(
            PipeBombEntity::new,
            SpawnGroup.MISC)
            .dimensions(0.25F, 0.25F)
            .maxTrackingRange(64)
            .trackingTickInterval(1)
            .alwaysUpdateVelocity(true)
            .build();

    public static final EntityType<JugglingBallEntity> JUGGLING_BALL = EntityType.Builder.create(
                    JugglingBallEntity::new,
                    SpawnGroup.MISC)
            .dimensions(0.45F, 0.45F)
            .maxTrackingRange(64)
            .trackingTickInterval(1)
            .alwaysUpdateVelocity(true)
            .build();

    @Override
    public Registry<EntityType<?>> getRegistry() { return Registries.ENTITY_TYPE; }
}
