package dev.spiritstudios.bombastic.main.particle;

import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class AdvancedParticle extends SpriteBillboardParticle {
    Quaternionf rotation;
    Quaternionf prevRotation;
    Vector3f rotationAxis;
    float rotationVelocity;

    protected AdvancedParticle(ClientWorld clientWorld, double x, double y, double z) {
        super(clientWorld, x, y, z);

        this.rotation = new Quaternionf();
        this.prevRotation = new Quaternionf();
        this.rotationAxis = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()).normalize();
        this.rotationVelocity = (this.random.nextFloat() - 0.5F) * 0.5F;
    }

    protected AdvancedParticle(ClientWorld clientWorld,
                               double x,
                               double y,
                               double z,
                               double velocityX,
                               double velocityY,
                               double velocityZ) {
        super(clientWorld, x, y, z, velocityX, velocityY, velocityZ);

        this.rotation = new Quaternionf();
        this.prevRotation = new Quaternionf();
        this.rotationAxis = new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()).normalize();
        this.rotationVelocity = (this.random.nextFloat() - 0.5F) * 0.5F;
    }

    Quaternionf getRotation(Camera camera, Quaternionf quaternionf) {
        Vector3f particleForward = new Vector3f(0, 0, -1).rotate(quaternionf);
        Vector3f cameraDelta = new Vector3f((float) x, (float) y, (float) z).sub(camera.getPos().toVector3f());

        if (particleForward.dot(cameraDelta) < 0) quaternionf.rotateY(MathHelper.PI);
        return quaternionf;
    }

    protected void renderQuad(VertexConsumer vertexConsumer, Vec3d offset, Camera camera, Quaternionf quaternionf, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float x = (float)(MathHelper.lerp(tickDelta, this.prevPosX + offset.x, this.x + offset.x) - vec3d.getX());
        float y = (float)(MathHelper.lerp(tickDelta, this.prevPosY + offset.y, this.y + offset.y) - vec3d.getY());
        float z = (float)(MathHelper.lerp(tickDelta, this.prevPosZ + offset.z, this.z + offset.z) - vec3d.getZ());

        this.method_60374(vertexConsumer, quaternionf, x, y, z, tickDelta);
    }

    protected void renderCube(VertexConsumer vertexConsumer, Camera camera, Quaternionf rotation, float tickDelta) {
        Quaternionf quaternionf = this.getRotation(camera, new Quaternionf().rotationXYZ(MathHelper.HALF_PI, 0, 0));
        Quaternionf quaternionf2 = this.getRotation(camera, new Quaternionf().rotationXYZ(0, MathHelper.HALF_PI, 0));
        Quaternionf quaternionf3 = this.getRotation(camera, new Quaternionf().rotationXYZ(0, 0, MathHelper.HALF_PI));

        quaternionf = quaternionf.mul(rotation);
        quaternionf2 = quaternionf2.mul(rotation);
        quaternionf3 = quaternionf3.mul(rotation);

        renderQuad(vertexConsumer, new Vec3d(0, scale, 0), camera, quaternionf, tickDelta);
        renderQuad(vertexConsumer, new Vec3d(0, -scale, 0), camera, quaternionf, tickDelta);

        renderQuad(vertexConsumer, new Vec3d(scale, 0, 0), camera, quaternionf2, tickDelta);
        renderQuad(vertexConsumer, new Vec3d(-scale, 0, 0), camera, quaternionf2, tickDelta);

        renderQuad(vertexConsumer, new Vec3d(0, 0, scale), camera, quaternionf3, tickDelta);
        renderQuad(vertexConsumer, new Vec3d(0, 0, -scale), camera, quaternionf3, tickDelta);
    }
}
