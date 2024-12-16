package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.block.ConfettiCannonBlock;
import dev.spiritstudios.bombastic.main.block.FirecrackerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;

@SuppressWarnings("unused")
public class BombasticBlocks {
    public static final ConfettiCannonBlock CONFETTI_CANNON = new ConfettiCannonBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DEEPSLATE_GRAY)
            .requiresTool()
            .strength(3.0F, 6.0F)
            .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final FirecrackerBlock FIRECRACKER = new FirecrackerBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never));
}
