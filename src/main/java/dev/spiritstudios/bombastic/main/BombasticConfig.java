package dev.spiritstudios.bombastic.main;

import dev.spiritstudios.specter.api.config.Config;
import dev.spiritstudios.specter.api.config.annotations.Range;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

public class BombasticConfig implements Config {
    @Range(min = 1.0F, max = 10.0F)
    public float clownBootsMultiplier = 7.5F;

    @Override
    public Identifier getId() { return Identifier.of(MODID, "bombastic"); }
}
