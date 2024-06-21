package dev.callmeecho.bombastic.client.render.entity;

import dev.callmeecho.bombastic.main.block.ConfettiCannonBlock;
import dev.callmeecho.bombastic.main.block.entity.ConfettiCannonBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ConfettiCannonBlockEntityRenderer implements BlockEntityRenderer<ConfettiCannonBlockEntity> {
    public static final Identifier TEXTURE = Identifier.of("bombastic", "textures/block/confetti_cannon.png");
    public static final Identifier TEXTURE_POWERED = Identifier.of("bombastic", "textures/block/confetti_cannon_powered.png");


    public ConfettiCannonBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("spinner", ModelPartBuilder.create().uv(0, 28).cuboid(-3.0F, -16.0F, -3.0F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-1.0F, -15.0F, -7.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F))
                .uv(30, 28).cuboid(3.0F, -15.0F, -1.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(18, 28).cuboid(-7.0F, -15.0F, -1.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -15.0F, 3.0F, 2.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0, 0.0F));

        modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -12.0F, -8.0F, 16.0F, 12.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(ConfettiCannonBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.scale(1, -1, 1);
        matrices.translate(0.5, 0, 0.5);

        TexturedModelData texturedModelData = getTexturedModelData();
        ModelPart spinner = texturedModelData.createModel().getChild("spinner");
        ModelPart bb_main = texturedModelData.createModel().getChild("bb_main");

        entity.age += 1;
        if (entity.age > 360) entity.age = 0;

        boolean powered = entity.getCachedState().get(ConfettiCannonBlock.POWERED);

        if (powered)
            spinner.yaw = (entity.age + tickDelta) * 0.075f;

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(powered ? TEXTURE_POWERED : TEXTURE));

        bb_main.render(matrices, vertexConsumer, light, overlay);
        spinner.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }
}
