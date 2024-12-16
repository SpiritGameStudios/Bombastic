package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.block.entity.ConfettiCannonBlockEntity;
import dev.spiritstudios.bombastic.main.block.entity.FirecrackerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class BombasticBlockEntities {
    public static final BlockEntityType<ConfettiCannonBlockEntity> CONFETTI_CANNON = BlockEntityType.Builder.create(
            ConfettiCannonBlockEntity::new,
            BombasticBlocks.CONFETTI_CANNON
    ).build(null);

    public static final BlockEntityType<FirecrackerBlockEntity> FIRECRACKER = BlockEntityType.Builder.create(
            FirecrackerBlockEntity::new,
            BombasticBlocks.FIRECRACKER
    ).build(null);
}
