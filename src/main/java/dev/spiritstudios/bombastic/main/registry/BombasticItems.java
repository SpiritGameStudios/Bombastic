package dev.spiritstudios.bombastic.main.registry;

import dev.spiritstudios.bombastic.main.item.BombasticArmorMaterials;
import dev.spiritstudios.bombastic.main.item.JugglingBallItem;
import dev.spiritstudios.bombastic.main.item.PartyPopperItem;
import dev.spiritstudios.bombastic.main.item.PipeBombItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;

@SuppressWarnings("unused")
public class BombasticItems {
    public static ArmorItem CLOWN_BOOTS = new ArmorItem(
            BombasticArmorMaterials.CLOWN,
            ArmorItem.Type.BOOTS,
            new Item.Settings().maxCount(1).maxDamage(100)
    );

    public static ArmorItem CLOWN_HAIR = new ArmorItem(
            BombasticArmorMaterials.CLOWN,
            ArmorItem.Type.HELMET,
            new Item.Settings().maxCount(1).maxDamage(100)
    );


    public static PipeBombItem PIPE_BOMB = new PipeBombItem(new Item.Settings().maxCount(16));
    public static PartyPopperItem PARTY_POPPER = new PartyPopperItem(new Item.Settings().maxCount(1).maxDamage(150));
    public static JugglingBallItem JUGGLING_BALL = new JugglingBallItem(new Item.Settings().maxCount(1).maxDamage(75));
}
