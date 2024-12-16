package dev.spiritstudios.bombastic.mixin;

import dev.spiritstudios.bombastic.main.registry.BombasticDataComponentTypes;
import dev.spiritstudios.bombastic.main.registry.BombasticItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("HEAD"))
    private void dropItem(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> cir) {
        if (!itemStack.isOf(BombasticItems.PIPE_BOMB)) return;

        itemStack.set(BombasticDataComponentTypes.PINNED, false);
    }
}
