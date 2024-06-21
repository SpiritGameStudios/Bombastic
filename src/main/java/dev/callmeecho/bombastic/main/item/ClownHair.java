package dev.callmeecho.bombastic.main.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public class ClownHair extends ArmorItem {
    public ClownHair(RegistryEntry<ArmorMaterial> registryEntry, Type type, Settings settings) {
        super(registryEntry, type, settings);
    }
}
