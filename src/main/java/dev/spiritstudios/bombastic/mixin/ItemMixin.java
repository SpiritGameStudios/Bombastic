package dev.spiritstudios.bombastic.mixin;

import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!user.isFallFlying() || !user.getStackInHand(Hand.MAIN_HAND).isOf(Items.TNT)) return;

        TntEntity tnt = new TntEntity(world, user.getX(), user.getY(), user.getZ(), user);
        tnt.setVelocity(user.getVelocity().multiply(1.25F));
        world.spawnEntity(tnt);
        world.playSound(tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.ENTITY_TNT_PRIMED, tnt.getSoundCategory(), 1.0F, 1.0F, false);
        user.emitGameEvent(GameEvent.PRIME_FUSE);

        if(!user.getAbilities().creativeMode) user.getStackInHand(Hand.MAIN_HAND).decrement(1);

        cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand), world.isClient));
    }
}
