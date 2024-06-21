package dev.callmeecho.bombastic.main.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;

import java.util.Optional;
import java.util.function.Supplier;

public class ChangingExplosionBehavior extends AdvancedExplosionBehavior {
    public float knockbackMultiplier;

    public ChangingExplosionBehavior(
            boolean destroyBlocks,
            boolean damageEntities,
            float knockbackMultiplier,
            Optional<RegistryEntryList<Block>> immuneBlocks
    ) {
        super(destroyBlocks, damageEntities, Optional.empty(), immuneBlocks);
        this.knockbackMultiplier = knockbackMultiplier;
    }

    @Override
    public float getKnockbackModifier(Entity entity) {
        return this.knockbackMultiplier;
    }
}
