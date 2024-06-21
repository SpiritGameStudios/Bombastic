package dev.callmeecho.bombastic.main;

import dev.callmeecho.bombastic.main.registry.BombasticDataComponentTypeRegistrar;
import dev.callmeecho.bombastic.main.registry.BombasticItemRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PipeBombRecipe extends SpecialCraftingRecipe {
    private static final Ingredient FIREWORK = Ingredient.ofItems(Items.FIREWORK_ROCKET);
    private static final Ingredient POTION = Ingredient.ofItems(Items.LINGERING_POTION);
    private static final Ingredient PARTY_POPPER = Ingredient.ofItems(BombasticItemRegistrar.PARTY_POPPER);
    private static final Ingredient TNT = Ingredient.ofItems(Items.TNT);

    private static final Ingredient IRON_INGOT = Ingredient.ofItems(Items.IRON_INGOT);

    public PipeBombRecipe(CraftingRecipeCategory craftingRecipeCategory) { super(craftingRecipeCategory); }

    @Override
    public boolean matches(CraftingRecipeInput recipeInput, World world) {
        int triggerCount = 0;
        int ironIngotCount = 0;
        for (int i = 0; i < recipeInput.getSize(); i++) {
            ItemStack itemStack = recipeInput.getStackInSlot(i);

            if (Stream.of(FIREWORK, PARTY_POPPER, POTION, TNT).anyMatch(ingredient -> ingredient.test(itemStack))) triggerCount++;
            else if (IRON_INGOT.test(itemStack)) ironIngotCount++;
            else if (!itemStack.isEmpty()) return false;
        }


        return triggerCount > 0 && ironIngotCount == 1;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack pipeBomb = new ItemStack(BombasticItemRegistrar.PIPE_BOMB);
        List<ItemStack> triggers = new ArrayList<>();
        int triggerCount = 0;
        int ironIngotCount = 0;
        for (int i = 0; i < recipeInput.getSize(); i++) {
            ItemStack itemStack = recipeInput.getStackInSlot(i);

            if (Stream.of(FIREWORK, PARTY_POPPER, POTION, TNT).anyMatch(ingredient -> ingredient.test(itemStack))) {
                triggerCount++;
                ItemStack trigger = itemStack.copy();
                trigger.setCount(1);
                triggers.add(trigger);
            }
            else if (IRON_INGOT.test(itemStack)) ironIngotCount++;
            else if (!itemStack.isEmpty()) return ItemStack.EMPTY;
        }

        if (triggerCount <= 0 || ironIngotCount != 1) return ItemStack.EMPTY;
        pipeBomb.set(BombasticDataComponentTypeRegistrar.TRIGGERS, triggers);
        return pipeBomb;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup wrapperLookup) {
        return new ItemStack(BombasticItemRegistrar.PIPE_BOMB);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Bombastic.PIPE_BOMB_RECIPE;
    }
}
