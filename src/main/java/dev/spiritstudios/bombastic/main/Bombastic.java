package dev.spiritstudios.bombastic.main;

import dev.spiritstudios.bombastic.main.network.PartyPopperS2CPacket;
import dev.spiritstudios.bombastic.main.registry.*;
import dev.spiritstudios.specter.api.registry.registration.Registrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
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

    public static final SpecialRecipeSerializer<PipeBombRecipe> PIPE_BOMB_RECIPE = new SpecialRecipeSerializer<>(PipeBombRecipe::new);

    @Override
    public void onInitialize() {
        Registrar.process(BombasticDataComponentTypeRegistrar.class, MODID);
        Registrar.process(BombasticItemRegistrar.class, MODID);
        Registrar.process(BombasticSoundEventRegistrar.class, MODID);
        Registrar.process(BombasticEntityTypeRegistrar.class, MODID);
        Registrar.process(BombasticParticleRegistrar.class, MODID);
        Registrar.process(BombasticBlockRegistrar.class, MODID);
        Registrar.process(BombasticBlockEntityRegistrar.class, MODID);
        Registrar.process(BombasticEnchantmentComponentTypeRegistrar.class, MODID);
        
        Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(MODID, "pipe_bomb"),
                PIPE_BOMB_RECIPE
        );

        PayloadTypeRegistry.playS2C().register(
                PartyPopperS2CPacket.ID,
                PartyPopperS2CPacket.CODEC
        );
    }
}
