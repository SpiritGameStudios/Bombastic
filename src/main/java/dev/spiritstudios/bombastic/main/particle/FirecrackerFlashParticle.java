package dev.spiritstudios.bombastic.main.particle;

import dev.spiritstudios.specter.api.core.util.Easing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;

public class FirecrackerFlashParticle extends SpriteBillboardParticle {
    protected FirecrackerFlashParticle(
            ClientWorld world,
            double x,
            double y,
            double z,
            double velocityX,
            double velocityY,
            double velocityZ
    ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = 0.0F;
        this.velocityY = 0.0F;
        this.velocityZ = 0.0F;

        this.scale = 2.0F;
        this.maxAge = 4;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.dead) return;

        for (int i = 0; i < 20; i++) {
            this.world.addParticle(
                    ParticleTypes.LAVA,
                    this.x,
                    this.y,
                    this.z,
                    (Math.random() - 0.5) * 0.15,
                    (Math.random() - 0.5) * 0.15,
                    (Math.random() - 0.5) * 0.15
            );
        }
    }

    @Override
    public float getSize(float tickDelta) {
        return (float) Easing.QUART.yoyoOutIn(Math.min(age + tickDelta, getMaxAge()), 0, scale, getMaxAge());
    }

    @Environment(EnvType.CLIENT)
    public static final class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType particleEffect,
                                       ClientWorld clientWorld,
                                       double x,
                                       double y,
                                       double z,
                                       double velocityX,
                                       double velocityY,
                                       double velocityZ) {
            FirecrackerFlashParticle particle = new FirecrackerFlashParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
