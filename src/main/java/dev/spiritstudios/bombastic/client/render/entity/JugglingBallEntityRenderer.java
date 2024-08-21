package dev.spiritstudios.bombastic.client.render.entity;

import dev.spiritstudios.bombastic.client.render.entity.model.JugglingBallEntityModel;
import dev.spiritstudios.bombastic.main.entity.JugglingBallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

@Environment(EnvType.CLIENT)
public class JugglingBallEntityRenderer extends EntityRenderer<JugglingBallEntity> {
    private static final Identifier TEXTURE = Identifier.of(MODID, "textures/entity/juggling_ball.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutout(TEXTURE);

    private final JugglingBallEntityModel<JugglingBallEntity> model;

    public JugglingBallEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new JugglingBallEntityModel<>(ctx.getPart(JugglingBallEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(JugglingBallEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        VertexConsumer consumer = ItemRenderer.getDirectItemGlintConsumer(
                vertexConsumers, LAYER, false, entity.getEnchanted()
        );


        float smoothedYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getYaw());
        float smoothedPitch = MathHelper.lerpAngleDegrees(tickDelta, entity.prevPitch, entity.getPitch());

        this.model.setAngles(entity, 0.0F, 0.0F, 0.0F, smoothedYaw, smoothedPitch);
        this.model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(JugglingBallEntity entity) {
        return TEXTURE;
    }
}
