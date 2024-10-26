package dev.spiritstudios.bombastic.main;

import dev.spiritstudios.specter.api.config.Config;
import dev.spiritstudios.specter.api.config.ConfigHolder;
import dev.spiritstudios.specter.api.config.Value;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

public class BombasticConfig extends Config<BombasticConfig> {
    public static final ConfigHolder<BombasticConfig, ?> HOLDER = ConfigHolder.builder(
            Identifier.of(MODID, "bombastic"), BombasticConfig.class
    ).build();

    public static final BombasticConfig INSTANCE = HOLDER.get();

    public Value<Float> clownBootsMultiplier = floatValue(7.5F)
            .range(1.0F, 10.0F)
            .sync()
            .build();
}
