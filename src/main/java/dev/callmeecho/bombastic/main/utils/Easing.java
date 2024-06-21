package dev.callmeecho.bombastic.main.utils;

import net.minecraft.util.math.MathHelper;

public enum Easing {
    LINEAR((time, start, end, duration) -> end * time / duration + start),

    SINE_IN((time, start, end, duration) ->  -end * (float)Math.cos(time / duration * MathHelper.HALF_PI) + end + start),
    SINE_OUT((time, start, end, duration) -> end * (float)Math.sin(time / duration * MathHelper.HALF_PI) + start),
    SINE_IN_OUT((time, start, end, duration) -> -end / 2 * ((float)Math.cos(Math.PI * time / duration) - 1) + start),

    QUAD_IN((time, start, end, duration) -> end * (time /= duration) * time + start),
    QUAD_OUT((time, start, end, duration) -> -end * (time /= duration) * (time - 2) + start),
    QUAD_IN_OUT((time, start, end, duration) -> {
        if ((time /= duration / 2) < 1) return end / 2 * time * time + start;
        return -end / 2 * ((--time) * (time - 2) - 1) + start;
    }),

    CUBIC_IN((time, start, end, duration) -> end * (time /= duration) * time * time + start),
    CUBIC_OUT((time, start, end, duration) -> end * ((time = time / duration - 1) * time * time + 1) + start),
    CUBIC_IN_OUT((time, start, end, duration) -> {
        if ((time /= duration / 2) < 1) return end / 2 * time * time * time + start;
        return end / 2 * ((time -= 2) * time * time + 2) + start;
    }),

    QUART_IN((time, start, end, duration) -> end * (time /= duration) * time * time * time + start),
    QUART_OUT((time, start, end, duration) -> -end * ((time = time / duration - 1) * time * time * time - 1) + start),
    QUART_IN_OUT((time, start, end, duration) -> {
        if ((time /= duration / 2) < 1) return end / 2 * time * time * time * time + start;
        return -end / 2 * ((time -= 2) * time * time * time - 2) + start;
    }),

    QUINT_IN((time, start, end, duration) -> end * (time /= duration) * time * time * time * time + start),
    QUINT_OUT((time, start, end, duration) -> end * ((time = time / duration - 1) * time * time * time * time + 1) + start),
    QUINT_IN_OUT((time, start, end, duration) -> {
        if ((time /= duration / 2) < 1) return end / 2 * time * time * time * time * time + start;
        return end / 2 * ((time -= 2) * time * time * time * time + 2) + start;
    }),

    EXP_IN((time, start, end, duration) -> time == 0 ? start : end * (float)Math.pow(2, 10 * (time / duration - 1)) + start),
    EXP_OUT((time, start, end, duration) -> time == duration ? start + end : end * (-(float)Math.pow(2, -10 * time / duration) + 1) + start),
    EXP_IN_OUT((time, start, end, duration) -> {
        if (time == 0) return start;
        if (time == duration) return start + end;
        if ((time /= duration / 2) < 1) return end / 2 * (float)Math.pow(2, 10 * (time - 1)) + start;
        return end / 2 * (-(float)Math.pow(2, -10 * --time) + 2) + start;
    }),

    CIRC_IN((time, start, end, duration) -> -end * ((float)Math.sqrt(1 - (time /= duration) * time) - 1) + start),
    CIRC_OUT((time, start, end, duration) -> end * (float)Math.sqrt(1 - (time = time / duration - 1) * time) + start),
    CIRC_IN_OUT((time, start, end, duration) -> {
        if ((time /= duration / 2) < 1) return -end / 2 * ((float)Math.sqrt(1 - time * time) - 1) + start;
        return end / 2 * ((float)Math.sqrt(1 - (time -= 2) * time) + 1) + start;
    });

    final EasingFunction function;
    Easing(EasingFunction function) { this.function = function; }

    public float apply(float time, float start, float end, float duration) { return function.apply(time, start, end, duration); }

    @FunctionalInterface
    public interface EasingFunction { float apply(float time, float start, float end, float duration); }
}
