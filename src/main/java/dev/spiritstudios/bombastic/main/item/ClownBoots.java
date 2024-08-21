package dev.spiritstudios.bombastic.main.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

public class ClownBoots extends ArmorItem {
    public ClownBoots(RegistryEntry<ArmorMaterial> registryEntry, Type type, Settings settings) {
        super(registryEntry, type, settings);
    }
}
