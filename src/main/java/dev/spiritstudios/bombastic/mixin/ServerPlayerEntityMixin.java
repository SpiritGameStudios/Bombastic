package dev.spiritstudios.bombastic.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.bombastic.main.registry.BombasticItemRegistrar;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract void setSpawnExtraParticlesOnFall(boolean bl);


    public ServerPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(world, blockPos, f, gameProfile);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (!itemStack.isOf(BombasticItemRegistrar.CLOWN_BOOTS)) return;

        if (this.isOnGround()) {
            this.setSpawnExtraParticlesOnFall(true);
        }
    }
}

