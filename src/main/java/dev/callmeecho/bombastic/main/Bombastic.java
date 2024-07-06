package dev.callmeecho.bombastic.main;

import dev.callmeecho.bombastic.main.registry.*;
import dev.callmeecho.cabinetapi.config.ConfigHandler;
import dev.callmeecho.cabinetapi.item.CabinetItemGroup;
import dev.callmeecho.cabinetapi.registry.RegistrarHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bombastic implements ModInitializer {
    public static final String MODID = "bombastic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static final CabinetItemGroup GROUP = new CabinetItemGroup(
            Identifier.of(MODID, "group"),
            () -> {
                ItemStack item = new ItemStack(BombasticItemRegistrar.PIPE_BOMB);
                item.set(BombasticDataComponentTypeRegistrar.PINNED, false);
                return item;
            }
    );

    public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = new SpecialRecipeSerializer<>(PipeBombRecipe::new);
    public static final BombasticConfig CONFIG = ConfigHandler.getConfig(BombasticConfig.class);

    @Override
    public void onInitialize() {
        RegistrarHandler.process(BombasticDataComponentTypeRegistrar.class, MODID);
        RegistrarHandler.process(BombasticItemRegistrar.class, MODID);
        RegistrarHandler.process(BombasticSoundEventRegistrar.class, MODID);
        RegistrarHandler.process(BombasticEntityTypeRegistrar.class, MODID);
        RegistrarHandler.process(BombasticParticleRegistrar.class, MODID);
        RegistrarHandler.process(BombasticBlockRegistrar.class, MODID);
        RegistrarHandler.process(BombasticBlockEntityRegistrar.class, MODID);
        RegistrarHandler.process(BombasticEnchantmentComponentTypeRegistrar.class, MODID);

        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(MODID, "pipe_bomb"),
                PIPE_BOMB_RECIPE
        );

        GROUP.initialize();
    }
}
