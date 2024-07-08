package dev.callmeecho.bombastic.main.block;

import com.mojang.serialization.MapCodec;
import dev.callmeecho.bombastic.main.block.entity.ConfettiCannonBlockEntity;
import dev.callmeecho.bombastic.main.registry.BombasticBlockEntityRegistrar;
import dev.callmeecho.bombastic.main.utils.VoxelShapeHelperFix;
import dev.callmeecho.cabinetapi.util.VoxelShapeHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ConfettiCannonBlock extends BlockWithEntity {
    public static final MapCodec<ConfettiCannonBlock> CODEC = createCodec(ConfettiCannonBlock::new);
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape UP = VoxelShapes.union(
            createCuboidShape(0, 0, 0, 16, 12, 16),
            createCuboidShape(5, 12, 5, 11, 16, 11),
            createCuboidShape(7, 12, 1, 9, 15, 5),
            createCuboidShape(1, 12, 7, 5, 15, 9),
            createCuboidShape(11, 12, 7, 15, 15, 9),
            createCuboidShape(7, 12, 11, 9, 15, 15)
    );

    @Override
    public MapCodec<ConfettiCannonBlock> getCodec() { return CODEC; }

    public ConfettiCannonBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false).with(FACING, Direction.UP));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) { return BlockRenderType.ENTITYBLOCK_ANIMATED; }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) return;

        boolean powered = state.get(POWERED);
        if (powered == world.isReceivingRedstonePower(pos)) return;

        if (powered) world.scheduleBlockTick(pos, this, 4);
        else world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.get(POWERED) || world.isReceivingRedstonePower(pos)) return;
        world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN -> VoxelShapeHelperFix.flipY(UP);
            case UP -> UP;

            case NORTH -> VoxelShapeHelper.rotate(Direction.NORTH, Direction.EAST, VoxelShapeHelperFix.rotateZ(UP));
            case SOUTH -> VoxelShapeHelper.rotate(Direction.SOUTH, Direction.EAST, VoxelShapeHelperFix.rotateZ(UP));
            case WEST -> VoxelShapeHelper.rotate(Direction.WEST, Direction.EAST, VoxelShapeHelperFix.rotateZ(UP));
            case EAST -> VoxelShapeHelperFix.rotateZ(VoxelShapeHelperFix.flipY(UP));
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(POWERED, FACING); }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(
                POWERED,
                ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos())
        ).with(
                FACING,
                ctx.getSide()
        );
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, BombasticBlockEntityRegistrar.CONFETTI_CANNON, ConfettiCannonBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) { return new ConfettiCannonBlockEntity(pos, state); }
}
