package dev.spiritstudios.bombastic.client;

import dev.spiritstudios.bombastic.client.render.entity.ClownBootsRenderer;
import dev.spiritstudios.bombastic.client.render.entity.ClownHairRenderer;
import dev.spiritstudios.bombastic.client.render.entity.ConfettiCannonBlockEntityRenderer;
import dev.spiritstudios.bombastic.client.render.entity.JugglingBallEntityRenderer;
import dev.spiritstudios.bombastic.client.render.entity.model.ClownBootsEntityModel;
import dev.spiritstudios.bombastic.client.render.entity.model.ClownHairEntityModel;
import dev.spiritstudios.bombastic.client.render.entity.model.JugglingBallEntityModel;
import dev.spiritstudios.bombastic.main.BombasticConfig;
import dev.spiritstudios.bombastic.main.network.PartyPopperS2CPacket;
import dev.spiritstudios.bombastic.main.particle.ConfettiParticle;
import dev.spiritstudios.bombastic.main.particle.FirecrackerFlashParticle;
import dev.spiritstudios.bombastic.main.registry.*;
import dev.spiritstudios.specter.api.ModMenuHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

public class BombasticClient implements ClientModInitializer {
    public static int freezeFrames = -1;

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BombasticEntityTypeRegistrar.PIPE_BOMB, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(BombasticEntityTypeRegistrar.JUGGLING_BALL, JugglingBallEntityRenderer::new);

        ModelPredicateProviderRegistry.register(
                BombasticItemRegistrar.PIPE_BOMB,
                Identifier.of("pipe_bomb_lit"),
                (itemStack, world, entity, i) ->
                        itemStack.getOrDefault(BombasticDataComponentTypeRegistrar.PINNED, true) ? 0.0F : 1.0F
        );

        EntityModelLayerRegistry.registerModelLayer(ClownBootsEntityModel.LAYER_LOCATION, ClownBootsEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ClownHairEntityModel.LAYER_LOCATION, ClownHairEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(JugglingBallEntityModel.LAYER_LOCATION, JugglingBallEntityModel::getTexturedModelData);

        ArmorRenderer.register(new ClownBootsRenderer(), BombasticItemRegistrar.CLOWN_BOOTS);
        ArmorRenderer.register(new ClownHairRenderer(), BombasticItemRegistrar.CLOWN_HAIR);

        ParticleFactoryRegistry.getInstance().register(BombasticParticleRegistrar.CONFETTI, ConfettiParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(BombasticParticleRegistrar.FIRECRACKER_FLASH, FirecrackerFlashParticle.Factory::new);

        BlockEntityRendererFactories.register(BombasticBlockEntityRegistrar.CONFETTI_CANNON, ConfettiCannonBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(PartyPopperS2CPacket.ID, PartyPopperS2CPacket::receive);

        if (!FabricLoader.getInstance().isModLoaded("axiom"))
            ModMenuHelper.addConfig(MODID, BombasticConfig.class);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (freezeFrames > 0) {
                freezeFrames--;
            } else if (freezeFrames == 0) {
                freezeFrames = -1;
                client.skipGameRender = false;
                ClientPlayerEntity player = client.player;
                if (player == null) return;
                player.playSound(
                        SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                        1.0F,
                        2.0f
                );
            }
        });
    }
}
