package dev.spiritstudios.bombastic.main;

import net.minecraft.util.math.random.Random;

public class RandomHelper {
    public static float nextFloatBetween(Random random, float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }
}
