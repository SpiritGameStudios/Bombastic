package dev.spiritstudios.bombastic.main.item;

import com.mojang.datafixers.util.Pair;
import dev.spiritstudios.bombastic.main.network.PartyPopperS2CPacket;
import dev.spiritstudios.bombastic.main.registry.BombasticEnchantmentComponentTypeRegistrar;
import dev.spiritstudios.bombastic.main.registry.BombasticParticleRegistrar;
import dev.spiritstudios.bombastic.main.registry.BombasticSoundEventRegistrar;
import dev.spiritstudios.bombastic.main.utils.ChangingExplosionBehavior;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.Function;

public class PartyPopperItem extends Item {
    private static final ChangingExplosionBehavior EXPLOSION_BEHAVIOR = new ChangingExplosionBehavior(
            false, false, 0.0F, Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
    );

    public PartyPopperItem(Settings settings) {
        super(settings);
    }

    private static void explode(World world, PlayerEntity playerEntity, float power) {
        world.playSound(
                null,
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                playerEntity.getSoundCategory(),
                1F,
                1.0F
        );

        playerEntity.getWorld()
                .createExplosion(
                        null,
                        null,
                        EXPLOSION_BEHAVIOR,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        power,
                        false,
                        World.ExplosionSourceType.TRIGGER,
                        BombasticParticleRegistrar.CONFETTI,
                        BombasticParticleRegistrar.CONFETTI,
                        Registries.SOUND_EVENT.getEntry(SoundEvents.INTENTIONALLY_EMPTY)
                );

        world.syncWorldEvent(WorldEvents.SMASH_ATTACK, playerEntity.getSteppingPos(), 750);
        playerEntity.setIgnoreFallDamageFromCurrentExplosion(true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient) return TypedActionResult.success(playerEntity.getStackInHand(hand));
        PartyPopperS2CPacket.PartyPopperPayload payload = new PartyPopperS2CPacket.PartyPopperPayload(
                new Pair<>(new Vec3d(playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ()).toVector3f(), playerEntity.getRotationVector().toVector3f()));

        PartyPopperS2CPacket.send(payload, (ServerPlayerEntity) playerEntity);
        for (ServerPlayerEntity player : PlayerLookup.tracking(playerEntity)) {
            PartyPopperS2CPacket.send(payload, player);
        }

        world.playSound(
                null,
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                BombasticSoundEventRegistrar.PARTY_POPPER,
                playerEntity.getSoundCategory(),
                1.0F,
                1.0F
        );

        ItemStack stack = playerEntity.getStackInHand(hand);


        MutableFloat power = new MutableFloat(0.0F);
        EnchantmentHelper.forEachEnchantment(
                stack,
                (enchantment, level) -> enchantment.value().modifyValue(
                        BombasticEnchantmentComponentTypeRegistrar.PARTY_POPPER_EXPLOSION,
                        playerEntity.getRandom(),
                        level,
                        power
                ));
        EXPLOSION_BEHAVIOR.knockbackMultiplier = power.floatValue();

        if (power.floatValue() > 0.0F)
            explode(world, playerEntity, power.floatValue());

        playerEntity.getItemCooldownManager().set(this, 20);
        stack.damage(1, playerEntity, EquipmentSlot.MAINHAND);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}