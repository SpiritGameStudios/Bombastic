package dev.callmeecho.bombastic.main.block.entity;

import dev.callmeecho.bombastic.main.block.ConfettiCannonBlock;
import dev.callmeecho.bombastic.main.registry.BombasticBlockEntityRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticParticleRegistrar;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConfettiCannonBlockEntity extends BlockEntity {
    public ConfettiCannonBlockEntity(BlockPos pos, BlockState state) { super(BombasticBlockEntityRegistrar.CONFETTI_CANNON, pos, state); }

    public int age;
    public static void tick(World world, BlockPos pos, BlockState state, ConfettiCannonBlockEntity blockEntity) {
        if (world.isClient) return;

        if (!state.get(ConfettiCannonBlock.POWERED)) return;

        for (int i = 0; i < 2; i++) {
            ((ServerWorld)world).spawnParticles(
                    BombasticParticleRegistrar.CONFETTI,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    0,
                    (Math.random() - 0.5) * 0.25,
                    Math.random(),
                    (Math.random() - 0.5) * 0.25,
                    1.0
            );
        }
    }
}
