package dev.spiritstudios.bombastic.main.block.entity;

import dev.spiritstudios.bombastic.main.block.ConfettiCannonBlock;
import dev.spiritstudios.bombastic.main.registry.BombasticBlockEntityRegistrar;
import dev.spiritstudios.bombastic.main.registry.BombasticParticleRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ConfettiCannonBlockEntity extends BlockEntity {
    public ConfettiCannonBlockEntity(BlockPos pos, BlockState state) { super(BombasticBlockEntityRegistrar.CONFETTI_CANNON, pos, state); }

    public int age;
    public static void tick(World world, BlockPos pos, BlockState state, ConfettiCannonBlockEntity blockEntity) {
        if (world.isClient) return;

        if (!state.get(ConfettiCannonBlock.POWERED)) return;

        Direction direction = state.get(ConfettiCannonBlock.FACING);
        float xOff = 0.5F + direction.getOffsetX() * 0.5F;
        float yOff = 0.5F + direction.getOffsetY() * 0.5F;
        float zOff = 0.5F + direction.getOffsetZ() * 0.5F;

        float velocityX = direction.getOffsetX() * 0.5F;
        float velocityY = direction.getOffsetY() * 0.5F;
        float velocityZ = direction.getOffsetZ() * 0.5F;

        if (velocityX == 0) velocityX = (float) (Math.random() - 0.5F) * 0.25F;
        if (velocityY == 0) velocityY = (float) (Math.random() - 0.5F) * 0.25F;
        if (velocityZ == 0) velocityZ = (float) (Math.random() - 0.5F) * 0.25F;

        for (int i = 0; i < 2; i++) {
            ((ServerWorld)world).spawnParticles(
                    BombasticParticleRegistrar.CONFETTI,
                    pos.getX() + xOff,
                    pos.getY() + yOff,
                    pos.getZ() + zOff,
                    0,
                    velocityX,
                    velocityY,
                    velocityZ,
                    1.0
            );
        }
    }
}
