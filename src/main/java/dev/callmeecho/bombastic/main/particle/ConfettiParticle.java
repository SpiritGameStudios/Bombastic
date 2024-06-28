package dev.callmeecho.bombastic.main.particle;

import dev.callmeecho.bombastic.main.Bombastic;
import dev.callmeecho.cabinetapi.util.Easing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ConfettiParticle extends SpriteBillboardParticle {
    Quaternionf rotation;
    Quaternionf prevRotation;
    Vector3f rotationAxis;
    float rotationVelocity;

    public static int randomColor(Random random) {
        int hue = random.nextInt(360);

        return java.awt.Color.HSBtoRGB(hue / 360.0F, 1, 1);
    }

    protected ConfettiParticle(
            ClientWorld world,
            double x,
            double y,
            double z,
            double velocityX,
            double velocityY,
            double velocityZ
    ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.scale *= 0.1F + this.random.nextFloat() * 0.5F;
        this.maxAge = 200 + this.random.nextInt(75);
        this.collidesWithWorld = true;

        int color = randomColor(this.random);
        this.red = (color >> 16 & 255) / 255.0F;
        this.green = (color >> 8 & 255) / 255.0F;
        this.blue = (color & 255) / 255.0F;

        this.alpha = 1.0F;

        this.gravityStrength = 0.25F + this.random.nextFloat() * 0.25F;

        this.rotationAxis = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()).normalize();
        this.rotationVelocity = (this.random.nextFloat() - 0.5F) * 0.5F;
        this.rotation = new Quaternionf().rotateAxis(rotationVelocity * 10.0f, rotationAxis.x, rotationAxis.y, rotationAxis.z);
        this.prevRotation = new Quaternionf(rotation);
    }

    @Override
    public void tick() {
        this.prevRotation.set(rotation);
        super.tick();

        this.rotation.mul(new Quaternionf().rotateAxis(rotationVelocity, rotationAxis.x, rotationAxis.y, rotationAxis.z));
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternionf = this.getRotation(camera, tickDelta);

        this.prevPosY += rotationAxis.x * 0.005F;
        this.y += rotationAxis.x * 0.005F;
        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
        this.prevPosY -= rotationAxis.x * 0.005F;
        this.y -= rotationAxis.x * 0.005F;
    }

    @Override
    public float getSize(float tickDelta) {
        return (float) Easing.QUINT.in(Math.min(age + tickDelta, getMaxAge()), scale, 0, getMaxAge());
    }

    Quaternionf getRotation(Camera camera, float tickDelta) {
        Quaternionf quaternionf;
        if (this.onGround) quaternionf = ParticleUtils.GROUND_QUATERNION;
        else quaternionf = new Quaternionf(prevRotation).slerp(rotation, tickDelta);

        Vector3f particleForward = new Vector3f(0, 0, -1).rotate(quaternionf);
        Vector3f cameraDelta = new Vector3f((float) x, (float) y, (float) z).sub(camera.getPos().toVector3f());

        if (particleForward.dot(cameraDelta) < 0) quaternionf.rotateY(MathHelper.PI);
        return quaternionf;
    }

    @Override
    public ParticleTextureSheet getType() { return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT; }

    @Environment(EnvType.CLIENT)
    public static final class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;
        public Factory(SpriteProvider spriteProvider) { this.spriteProvider = spriteProvider; }

        @Override
        public Particle createParticle(SimpleParticleType particleEffect,
                                       ClientWorld clientWorld,
                                       double x,
                                       double y,
                                       double z,
                                       double velocityX,
                                       double velocityY,
                                       double velocityZ) {
            ConfettiParticle particle = new ConfettiParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
