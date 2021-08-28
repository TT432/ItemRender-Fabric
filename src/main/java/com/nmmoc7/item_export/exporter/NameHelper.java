package com.nmmoc7.item_export.exporter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NameHelper {
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

    public static ArrayList<Tag.Identified<Item>> getTags(Item item) {
        ArrayList<Tag.Identified<Item>> result = new ArrayList<>();

        ItemTags.getRequiredTags().stream().filter(itemIdentified -> itemIdentified.contains(item)).forEach(result::add);

        return result;
    }

    public static final NameHelper INSTANCE = new NameHelper();

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void scan() {
        scan(Type.en_us);
        scan(Type.zh_cn);
    }

    public void scan(Type type) {
        refreshLanguage(type.name());
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group != ItemGroup.HOTBAR && group != ItemGroup.INVENTORY) {
                DefaultedList<ItemStack> creativeTabItemStacks = DefaultedList.of();
                group.appendStacks(creativeTabItemStacks);

                for (ItemStack creativeTabItemStack : creativeTabItemStacks) {
                    getOrCreateModItems(creativeTabItemStack).add(creativeTabItemStack, type);
                }
            }
        }
    }

    private void refreshLanguage(String lang) {
        if (!mc.options.language.equals(lang)) {
            LanguageDefinition langDef = new LanguageDefinition(lang, "", "", false);
            mc.getLanguageManager().setLanguage(langDef);
            mc.options.language = langDef.getCode();
            mc.reloadResources();
            mc.options.write();
            mc.getLanguageManager().reload(mc.getResourceManager());
        }
    }

    public final Map<String, ModItems> MOD_ITEMS = new HashMap<>();

    public ModItems getOrCreateModItems(ItemStack itemStack) {
        String modName = getRegNameI(itemStack.getItem()).getNamespace();
        ModItems temp = MOD_ITEMS.get(modName);

        if (temp == null) {
            temp = new ModItems();
            MOD_ITEMS.put(modName, temp);
        }

        return temp;
    }

    public static class ModItems {
        Map<Item, FormatJson> map = new HashMap<>();

        public void add(ItemStack itemStack, Type type) {
            FormatJson temp = map.get(itemStack.getItem());

            if (temp == null) {
                temp = new FormatJson(itemStack);
                map.put(itemStack.getItem(), temp);
            }

            if (type == Type.en_us) {
                temp.setEnName(itemStack.getName().getString());
            }
            else {
                temp.setZhName(itemStack.getName().getString());
            }
        }
    }

    enum Type {
        en_us,
        zh_cn
    }
}
