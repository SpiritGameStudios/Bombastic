package dev.callmeecho.bombastic.main.block.entity;

import dev.callmeecho.bombastic.main.block.ConfettiCannonBlock;
import dev.callmeecho.bombastic.main.block.FirecrackerBlock;
import dev.callmeecho.bombastic.main.registry.BombasticBlockEntityRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticBlockRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticParticleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FirecrackerBlockEntity extends BlockEntity {
    private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(
            false, true, Optional.of(1F), Registries.BLOCK.getEntryList(BlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity())
    );

    public FirecrackerBlockEntity(BlockPos pos, BlockState state) {
        super(BombasticBlockEntityRegistrar.FIRECRACKER, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FirecrackerBlockEntity blockEntity) {
        if (world.isClient) return;

        if (state.get(FirecrackerBlock.UNSTABLE))
            explode(world, pos);
    }

    public static void explode(World world, BlockPos pos) {
        explode(world, pos, null);
    }

    private static void explode(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) return;

        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = pos.offset(direction);
            BlockState offsetState = world.getBlockState(offsetPos);
            if (offsetState.isOf(BombasticBlockRegistrar.FIRECRACKER))
                world.setBlockState(offsetPos, offsetState.with(FirecrackerBlock.UNSTABLE, true));
            else if (offsetState.isOf(Blocks.TNT)) {
                TntBlock.primeTnt(world, offsetPos);

                world.setBlockState(offsetPos, Blocks.AIR.getDefaultState());
            }
        }

        world.createExplosion(
                null,
                Explosion.createDamageSource(world, igniter),
                EXPLOSION_BEHAVIOR,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                1F,
                false,
                World.ExplosionSourceType.TNT,
                false,
                BombasticParticleRegistrar.NULL,
                BombasticParticleRegistrar.NULL,
                Registries.SOUND_EVENT.getEntry(SoundEvents.INTENTIONALLY_EMPTY)
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
                SoundCategory.BLOCKS,
                1F,
                (float)Math.random() + 1.25F
        );

        ((ServerWorld)world).spawnParticles(
                BombasticParticleRegistrar.FIRECRACKER_FLASH,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                0,
                0,
                0,
                0,
                0.0
        );

        world.removeBlock(pos, false);
    }
}
