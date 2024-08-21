package dev.spiritstudios.bombastic.main.item;

import dev.spiritstudios.bombastic.main.entity.JugglingBallEntity;
import dev.spiritstudios.bombastic.main.registry.BombasticSoundEventRegistrar;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class JugglingBallItem extends Item {
    public JugglingBallItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.success(user.getStackInHand(hand));

        ItemStack stack = user.getStackInHand(hand);
        ItemStack copy = stack.copy();

        copy.setCount(1);
        copy.damage(1, user, EquipmentSlot.MAINHAND);

        JugglingBallEntity ball = JugglingBallEntity.create(
                world,
                user,
                copy,
                user.getInventory().getSlotWithStack(stack)
        );

        ball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0.0F);
        world.spawnEntity(ball);
        stack.decrement(1);

        user.getItemCooldownManager().set(this, 10);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), BombasticSoundEventRegistrar.JUGGLING_BALL, user.getSoundCategory(), 1.0F, 1.0F);

        return TypedActionResult.success(stack);
    }
}
