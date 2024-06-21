package dev.callmeecho.bombastic.main.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TrailParticle extends AdvancedParticle {
    protected TrailParticle(
            ClientWorld world,
            double x,
            double y,
            double z
    ) {
        super(world, x, y, z, 0, 0, 0);

        this.scale = 0.25F;
        this.alpha = 0.5F;
        this.rotation = new Quaternionf();
        this.prevRotation = new Quaternionf();
        this.rotationAxis = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()).normalize();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        this.alpha -= 0.05f;
        this.scale -= 0.025f;
        if (this.scale <= 0f) this.markDead();
    }

    Quaternionf rotation;
    Quaternionf prevRotation;
    Vector3f rotationAxis;

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        renderCube(vertexConsumer, camera, rotation, tickDelta);
    }

    @Environment(EnvType.CLIENT)
    public static final class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            TrailParticle particle = new TrailParticle(world, x, y, z);
            particle.setSprite(this.spriteProvider);
            return particle;        }
    }
}
