package dev.spiritstudios.bombastic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.spiritstudios.bombastic.client.BombasticClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final
    MinecraftClient client;

    @Unique
    private static final Identifier PARRY_FLASH_TEXTURE = Identifier.of(MODID, "textures/gui/parry_flash.png");

    @Inject(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;",
            ordinal = 0,
            shift = At.Shift.BEFORE
    ))
    private void render(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, @Local DrawContext drawContext) {
        if (BombasticClient.freezeFrames == -1) return;

        client.skipGameRender = true;

        RenderSystem.enableBlend();
        drawContext.drawTexture(PARRY_FLASH_TEXTURE, 0, 0, 0, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), 32, 32);
        RenderSystem.disableBlend();
    }
}
