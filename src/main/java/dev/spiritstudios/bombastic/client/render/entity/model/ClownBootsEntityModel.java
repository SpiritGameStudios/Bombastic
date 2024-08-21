package dev.spiritstudios.bombastic.client.render.entity.model;

import dev.spiritstudios.bombastic.main.Bombastic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ClownBootsEntityModel<T extends LivingEntity> extends BipedEntityModel<T> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Identifier.of(Bombastic.MODID, "clown_boots"), "main");

    public ClownBootsEntityModel(ModelPart root) {
        super(root);
    }

    public static ModelData getModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(1.02f), 0.0F);
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                "left_leg",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-2.5F, 7.5F, -8.0F, 5.0F, 5.0F, 10.5F)
                        .uv(0, 16)
                        .cuboid(-2.5F, 2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        modelPartData.addChild(
                "right_leg",
                ModelPartBuilder.create()
                        .uv(0, 0).mirrored()
                        .cuboid(-2.5F, 7.5F, -8.0F, 5.0F, 5.0F, 10.5F)
                        .uv(0, 16).mirrored()
                        .cuboid(-2.5F, 2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return modelData;
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(getModelData(), 32, 32);
    }
}
