package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.Bombastic;
import dev.spiritstudios.bombastic.main.item.*;
import dev.spiritstudios.specter.api.registry.registration.ItemRegistrar;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class BombasticItemRegistrar implements ItemRegistrar {
    public static ClownBoots CLOWN_BOOTS = new ClownBoots(
            BombasticArmorMaterials.CLOWN,
            ArmorItem.Type.BOOTS,
            new Item.Settings().maxCount(1).maxDamage(100)
    );

    public static ClownHair CLOWN_HAIR = new ClownHair(
            BombasticArmorMaterials.CLOWN,
            ArmorItem.Type.HELMET,
            new Item.Settings().maxCount(1).maxDamage(100)
    );


    public static PipeBombItem PIPE_BOMB = new PipeBombItem(new Item.Settings().maxCount(16));
    public static PartyPopperItem PARTY_POPPER = new PartyPopperItem(new Item.Settings().maxCount(1).maxDamage(150));
    public static JugglingBallItem JUGGLING_BALL = new JugglingBallItem(new Item.Settings().maxCount(1).maxDamage(75));


    @Override
    public void register(String name, String namespace, Item object, Field field) {
        Registry.register(getRegistry(), Identifier.of(namespace, name), object);
        Bombastic.GROUP.addItem(object);
    }
}
