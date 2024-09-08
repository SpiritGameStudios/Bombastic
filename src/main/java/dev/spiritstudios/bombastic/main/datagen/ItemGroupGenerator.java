package dev.spiritstudios.bombastic.main.datagen;

import dev.spiritstudios.bombastic.main.registry.BombasticBlockRegistrar;
import dev.spiritstudios.bombastic.main.registry.BombasticItemRegistrar;
import dev.spiritstudios.specter.api.core.util.ReflectionHelper;
import dev.spiritstudios.specter.api.item.datagen.SpecterItemGroupProvider;
import dev.spiritstudios.specter.impl.item.DataItemGroup;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static dev.spiritstudios.bombastic.main.Bombastic.MODID;

public class ItemGroupGenerator extends SpecterItemGroupProvider {
    public ItemGroupGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    protected void generate(BiConsumer<Identifier, ItemGroupData> provider, RegistryWrapper.WrapperLookup lookup) {
        List<ItemStack> items = new ArrayList<>();
        ReflectionHelper.forEachStaticField(
                BombasticBlockRegistrar.class,
                Block.class,
                (block, name, field) -> {
                    ItemStack stack = new ItemStack(block.asItem());
                    if (!stack.isEmpty()) items.add(stack);
                }
        );
        ReflectionHelper.forEachStaticField(
                BombasticItemRegistrar.class,
                Item.class,
                (item, name, field) -> {
                    ItemStack stack = new ItemStack(item);
                    if (!stack.isEmpty()) items.add(stack);
                }
        );


        provider.accept(
                Identifier.of(MODID, "bombastic"),
                ItemGroupData.of(
                        Identifier.of(MODID, "bombastic"),
                        BombasticItemRegistrar.PIPE_BOMB,
                        items
                )
        );
    }
}
