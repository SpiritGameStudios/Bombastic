package dev.callmeecho.bombastic.main.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;

import static dev.callmeecho.bombastic.main.Bombastic.MODID;

public class BombasticArmorMaterials {
    public static final RegistryEntry<ArmorMaterial> CLOWN = register("clown", new ArmorMaterial(
            Util.make(new EnumMap(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 2);
                enumMap.put(ArmorItem.Type.LEGGINGS, 5);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 6);
                enumMap.put(ArmorItem.Type.HELMET, 2);
                enumMap.put(ArmorItem.Type.BODY, 5);
            }),
            15,
            SoundEvents.ITEM_ARMOR_EQUIP_LEATHER,
            () -> Ingredient.ofItems(Items.RED_WOOL),
            List.of(new ArmorMaterial.Layer(Identifier.of(MODID, "clown"), "", true)),
            0.0F,
            0.0F
    ));


    private static RegistryEntry<ArmorMaterial> register(String id, ArmorMaterial material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(MODID, id), material);
    }
}
