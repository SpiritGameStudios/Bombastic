package dev.callmeecho.bombastic.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.callmeecho.bombastic.client.BombasticClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @WrapWithCondition(method = "updateMouse", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"
    ))
    private boolean updateMouse(ClientPlayerEntity instance, double cursorDeltaX, double cursorDeltaY) {
        return BombasticClient.freezeFrames == -1;
    }
}
