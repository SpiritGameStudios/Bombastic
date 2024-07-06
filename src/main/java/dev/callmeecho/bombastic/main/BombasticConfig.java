package dev.callmeecho.bombastic.main;

import dev.callmeecho.cabinetapi.config.Config;
import dev.callmeecho.cabinetapi.config.annotations.Range;
import net.minecraft.util.Identifier;

import static dev.callmeecho.bombastic.main.Bombastic.MODID;

public class BombasticConfig implements Config {
    @Range(min = 1.0F, max = 10.0F)
    public float clownBootsMultiplier = 7.5F;

    @Override
    public Identifier getName() { return Identifier.of(MODID, "bombastic"); }
}
