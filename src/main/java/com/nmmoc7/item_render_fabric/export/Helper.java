package com.nmmoc7.item_render_fabric.export;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;

public class Helper {
    public static Identifier getRegNameI(Item item) {
        return Registry.ITEM.getId(item);
    }

    public static Identifier getRegNameI(EntityType<LivingEntity> entity) {
        return Registry.ENTITY_TYPE.getId(entity);
    }

    public static String getRegName(Item item) {
        return getRegNameI(item).toString();
    }

    public static String getRegName(EntityType<LivingEntity> entity) {
        return getRegNameI(entity).toString();
    }

    public static String getTags(Item item) {
        ArrayList<Tag.Identified<Item>> result = new ArrayList<>();

        ItemTags.getRequiredTags().stream().filter(itemIdentified -> itemIdentified.contains(item)).forEach(result::add);

        return result.toString();
    }
}
