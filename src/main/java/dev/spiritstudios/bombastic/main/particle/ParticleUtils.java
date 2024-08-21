package dev.spiritstudios.bombastic.main.particle;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public final class ParticleUtils {
    public static final Quaternionf GROUND_QUATERNION = new Quaternionf().rotationXYZ(-MathHelper.HALF_PI, 0, 0);
}
