package dev.callmeecho.bombastic.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import static dev.callmeecho.bombastic.main.Bombastic.MODID;

@Environment(EnvType.CLIENT)
public class JugglingBallEntityModel<T extends Entity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Identifier.of(MODID, "juggling_ball"), "main");

    private final ModelPart root;
    private final ModelPart main;

    public JugglingBallEntityModel(ModelPart root) {
        this.root = root;
        this.main = root.getChild("main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                "main",
                ModelPartBuilder.create()
                        .uv(12, 4)
                        .cuboid(-2.0F, 1.0F, -2.0F, 4.0F, 1.0F, 4.0F)
                        .uv(20, 17)
                        .cuboid(-2.0F, 2.0F, -3.0F, 4.0F, 4.0F, 1.0F)
                        .uv(0, 8)
                        .cuboid(-2.0F, 6.0F, -2.0F, 4.0F, 1.0F, 4.0F)
                        .uv(10, 17)
                        .cuboid(-2.0F, 2.0F, 2.0F, 4.0F, 4.0F, 1.0F)
                        .uv(0, 13)
                        .cuboid(2.0F, 2.0F, -2.0F, 1.0F, 4.0F, 4.0F)
                        .uv(12, 9)
                        .cuboid(-3.0F, 2.0F, -2.0F, 1.0F, 4.0F, 4.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public ModelPart getPart() { return this.root; }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.main.yaw = headYaw * (float) (Math.PI / 180.0);
        this.main.pitch = headPitch * (float) (Math.PI / 180.0);
    }
}
