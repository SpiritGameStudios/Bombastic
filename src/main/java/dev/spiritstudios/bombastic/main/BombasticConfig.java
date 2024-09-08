package dev.spiritstudios.bombastic.main;

import dev.spiritstudios.specter.api.config.Config;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

public class BombasticConfig extends Config<BombasticConfig> {
    public static final BombasticConfig INSTANCE = create(BombasticConfig.class);

    public Value<Float> clownBootsMultiplier = floatValue(7.5F)
            .range(1.0F, 10.0F)
            .sync()
            .build();


    @Override
    public Identifier getId() { return Identifier.of(MODID, "bombastic"); }
}
