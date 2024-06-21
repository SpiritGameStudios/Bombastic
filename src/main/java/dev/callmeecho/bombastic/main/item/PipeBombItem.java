package dev.callmeecho.bombastic.main.item;

import dev.callmeecho.bombastic.main.entity.PipeBombEntity;
import dev.callmeecho.bombastic.main.registry.BombasticDataComponentTypeRegistrar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class PipeBombItem extends Item {
    public PipeBombItem(Settings settings) { super(settings); }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemStack, world, entity, slot, selected);

        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity)) return;
        if (itemStack.getOrDefault(BombasticDataComponentTypeRegistrar.PINNED, true) && !selected) return;

        for (int i = 0; i < itemStack.getCount(); i++) {
            entity.playSound(SoundEvents.BLOCK_TRIPWIRE_CLICK_OFF, 1.0F, 1.0F);
            PipeBombEntity pipeBomb = PipeBombEntity.create(world, itemStack, (PlayerEntity)entity);
            world.spawnEntity(pipeBomb);

            itemStack.decrement(1);
        }
    }
}
