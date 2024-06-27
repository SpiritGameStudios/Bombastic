package dev.callmeecho.bombastic.mixin;

import dev.callmeecho.bombastic.client.BombasticClient;
import dev.callmeecho.bombastic.main.registry.BombasticSoundEventRegistrar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.callmeecho.bombastic.main.Bombastic.MODID;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {
    public ProjectileEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private static final TagKey<EntityType<?>> PARRIABLE_PROJECTILE = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MODID, "parriable_projectile"));

    @Inject(method = "deflect", at = @At("HEAD"))
    private void deflect(ProjectileDeflection deflection, Entity deflector, Entity owner, boolean fromAttack, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getWorld().isClient || !this.getType().isIn(PARRIABLE_PROJECTILE)) return;

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.playSound(
                BombasticSoundEventRegistrar.PARRY,
                1.0F,
                2.0F
        );

        if (player == owner)
            BombasticClient.freezeFrames = 5;
    }
}
