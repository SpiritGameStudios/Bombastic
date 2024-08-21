package dev.spiritstudios.bombastic.main.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.bombastic.main.block.entity.FirecrackerBlockEntity;
import dev.spiritstudios.bombastic.main.registry.BombasticBlockEntityRegistrar;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class FirecrackerBlock extends BlockWithEntity {
    public static final BooleanProperty UNSTABLE = Properties.UNSTABLE;
    public static final MapCodec<FirecrackerBlock> CODEC = createCodec(FirecrackerBlock::new);

    public FirecrackerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(UNSTABLE, false));
    }



    @Override
    public MapCodec<FirecrackerBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            if (world.isReceivingRedstonePower(pos))
                world.setBlockState(pos, state.with(UNSTABLE, true), Block.NOTIFY_NEIGHBORS);
        }
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos))
            world.setBlockState(pos, state.with(UNSTABLE, true), Block.NOTIFY_NEIGHBORS);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE))
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);

        world.setBlockState(pos, state.with(UNSTABLE, true), Block.NOTIFY_NEIGHBORS);

        Item item = stack.getItem();
        if (stack.isOf(Items.FLINT_AND_STEEL)) stack.damage(1, player, LivingEntity.getSlotForHand(hand));
        else stack.decrementUnlessCreative(1, player);

        player.incrementStat(Stats.USED.getOrCreateStat(item));
        return ItemActionResult.success(world.isClient);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.MODEL; }

    @Override
    protected void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient) {
            BlockPos blockPos = hit.getBlockPos();
            if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos))
                world.setBlockState(blockPos, state.with(UNSTABLE, true), Block.NOTIFY_NEIGHBORS);
        }
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (world.isClient) return;

        FirecrackerBlockEntity.explode(world, pos);
    }

    @Override
    public boolean shouldDropItemsOnExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(UNSTABLE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BombasticBlockEntityRegistrar.FIRECRACKER, FirecrackerBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FirecrackerBlockEntity(pos, state);
    }
}
