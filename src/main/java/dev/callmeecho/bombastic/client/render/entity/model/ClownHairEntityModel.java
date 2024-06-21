package dev.callmeecho.bombastic.client.render.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import static dev.callmeecho.bombastic.main.Bombastic.MODID;

@Environment(EnvType.CLIENT)
public class ClownHairEntityModel<T extends LivingEntity> extends BipedEntityModel<T> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(Identifier.of(MODID, "clown_hair"), "main");

    public ClownHairEntityModel(ModelPart root) {
        super(root);
    }

    public static ModelData getModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(1.02f), 0.0F);
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild(
                "head",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-4.0F, -10.0F, -5.0F, 9.0F, 2.0F, 9.0F)
                        .uv(0, 11)
                        .cuboid(-2.0F, -11.0F, -4.0F, 5.0F, 1.0F, 6.0F)
                        .uv(15, 11)
                        .cuboid(-5.0F, -8.0F, -3.0F, 1.0F, 4.0F, 7.0F)
                        .uv(18, 22)
                        .cuboid(4.0F, -8.0F, -2.0F, 2.0F, 3.0F, 6.0F)
                        .uv(0, 22)
                        .cuboid(-4.0F, -8.0F, 4.0F, 8.0F, 6.0F, 1.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return modelData;
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(getModelData(), 64, 64);
    }
}
