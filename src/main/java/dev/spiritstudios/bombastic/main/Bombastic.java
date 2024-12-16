package dev.spiritstudios.bombastic.main;

import dev.spiritstudios.bombastic.main.network.PartyPopperS2CPacket;
import dev.spiritstudios.bombastic.main.registry.BombasticBlockEntities;
import dev.spiritstudios.bombastic.main.registry.BombasticBlocks;
import dev.spiritstudios.bombastic.main.registry.BombasticDataComponentTypes;
import dev.spiritstudios.bombastic.main.registry.BombasticEnchantmentComponentTypes;
import dev.spiritstudios.bombastic.main.registry.BombasticEntityTypes;
import dev.spiritstudios.bombastic.main.registry.BombasticItems;
import dev.spiritstudios.bombastic.main.registry.BombasticParticleTypes;
import dev.spiritstudios.bombastic.main.registry.BombasticSoundEvents;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.component.ComponentType;
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
        RegistryHelper.registerDataComponentTypes(BombasticDataComponentTypes.class, MODID);
        RegistryHelper.registerItems(BombasticItems.class, MODID);
        RegistryHelper.registerSoundEvents(BombasticSoundEvents.class, MODID);
        RegistryHelper.registerEntityTypes(BombasticEntityTypes.class, MODID);
        RegistryHelper.registerParticleTypes(BombasticParticleTypes.class, MODID);
        RegistryHelper.registerBlocks(BombasticBlocks.class, MODID);
        RegistryHelper.registerBlockEntityTypes(BombasticBlockEntities.class, MODID);
        RegistryHelper.registerFields(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, RegistryHelper.fixGenerics(ComponentType.class), BombasticEnchantmentComponentTypes.class, MODID);

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
