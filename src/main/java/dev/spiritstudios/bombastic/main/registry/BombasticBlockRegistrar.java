package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.block.ConfettiCannonBlock;
import dev.spiritstudios.bombastic.main.block.FirecrackerBlock;
import dev.spiritstudios.bombastic.main.Bombastic;
import dev.spiritstudios.specter.api.registry.registration.BlockRegistrar;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class BombasticBlockRegistrar implements BlockRegistrar {
    public static final ConfettiCannonBlock CONFETTI_CANNON = new ConfettiCannonBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DEEPSLATE_GRAY)
            .requiresTool()
            .strength(3.0F, 6.0F)
            .sounds(BlockSoundGroup.DEEPSLATE)
    );

    public static final FirecrackerBlock FIRECRACKER = new FirecrackerBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).breakInstantly().sounds(BlockSoundGroup.GRASS).burnable().solidBlock(Blocks::never));
}
