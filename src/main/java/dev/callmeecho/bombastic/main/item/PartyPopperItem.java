package dev.callmeecho.bombastic.main.item;

import dev.callmeecho.bombastic.main.registry.BombasticEnchantmentComponentTypeRegistry;
import dev.callmeecho.bombastic.main.registry.BombasticParticleRegistrar;
import dev.callmeecho.bombastic.main.utils.ChangingExplosionBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient) return TypedActionResult.success(playerEntity.getStackInHand(hand));
        Vec3d direction = playerEntity.getRotationVector();
        Random random = playerEntity.getRandom();
        for (int i = 0; i < 250; i++) {
            Vec3d velocity = new Vec3d(
                    random.nextGaussian() * 0.15,
                    random.nextGaussian() * 0.15,
                    random.nextGaussian() * 0.15
            );
            velocity = velocity.add(direction.multiply(0.75F));

            ((ServerWorld) world).spawnParticles(
                    BombasticParticleRegistrar.CONFETTI,
                    playerEntity.getX(),
                    playerEntity.getEyeY() - 0.25F,
                    playerEntity.getZ(),
                    0,
                    velocity.getX(),
                    velocity.getY(),
                    velocity.getZ(),
                    1.0F
            );
        }

        world.playSound(
                null,
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH,
                playerEntity.getSoundCategory(),
                1.0F,
                1.0F
        );

        ItemStack stack = playerEntity.getStackInHand(hand);


        MutableFloat power = new MutableFloat(0.0F);
        EnchantmentHelper.forEachEnchantment(
                stack,
                (enchantment, level) -> enchantment.value().modifyValue(
                        BombasticEnchantmentComponentTypeRegistry.PARTY_POPPER_EXPLOSION,
                        random,
                        level,
                        power
                ));
        EXPLOSION_BEHAVIOR.knockbackMultiplier = power.floatValue();

        if (power.floatValue() > 0.0F)
            explode(world, playerEntity, power.floatValue());

        world.syncWorldEvent(WorldEvents.SMASH_ATTACK, playerEntity.getSteppingPos(), 750);
        playerEntity.setIgnoreFallDamageFromCurrentExplosion(true);

        playerEntity.getItemCooldownManager().set(this, 20);
        stack.damage(1, playerEntity, EquipmentSlot.MAINHAND);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
