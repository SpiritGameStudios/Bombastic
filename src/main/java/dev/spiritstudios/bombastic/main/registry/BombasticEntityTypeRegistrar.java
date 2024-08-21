package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.entity.JugglingBallEntity;
import dev.spiritstudios.bombastic.main.entity.PipeBombEntity;
import dev.spiritstudios.specter.api.registry.registration.EntityTypeRegistrar;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

@SuppressWarnings("unused")
public class BombasticEntityTypeRegistrar implements EntityTypeRegistrar {
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
}
