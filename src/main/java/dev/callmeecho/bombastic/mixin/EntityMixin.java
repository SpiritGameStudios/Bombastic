package dev.callmeecho.bombastic.mixin;

import dev.callmeecho.bombastic.main.RandomHelper;
import dev.callmeecho.bombastic.main.registry.BombasticItemRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticSoundEventRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public abstract World getWorld();

    @Shadow @Final protected Random random;

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Redirect(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getSoundGroup()Lnet/minecraft/sound/BlockSoundGroup;"))
    private BlockSoundGroup playStepSound(BlockState instance) {
        if (!((Object)this instanceof PlayerEntity playerEntity)) return instance.getSoundGroup();
        ItemStack itemStack = playerEntity.getEquippedStack(EquipmentSlot.FEET);
        if (!itemStack.isOf(BombasticItemRegistrar.CLOWN_BOOTS)) return instance.getSoundGroup();

        return BlockSoundGroup.SLIME;
    }


    @Inject(method = "onExplodedBy", at = @At("RETURN"))
    private void onExplodedBy(Entity entity, CallbackInfo ci) {
        if (!((Object)this instanceof ServerPlayerEntity playerEntity)) return;
        ItemStack itemStack = playerEntity.getEquippedStack(EquipmentSlot.FEET);
        if (!itemStack.isOf(BombasticItemRegistrar.CLOWN_BOOTS)) return;

        playerEntity.networkHandler.sendPacket(
                new EntityVelocityUpdateS2CPacket(
                        playerEntity.getId(),
                        playerEntity.getVelocity().multiply(7.5F, 0.0F, 7.5F)
                )
        );

        getWorld().playSound(
                null,
                getX(),
                getY(),
                getZ(),
                BombasticSoundEventRegistrar.CLOWN_BOOTS,
                SoundCategory.PLAYERS,
                5.0F,
                RandomHelper.nextFloatBetween(random, 0.8F, 1.2F)
        );
    }
}
