//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nmmoc7.item_render_fabric.export;

import com.nmmoc7.item_render_fabric.ItemRenderFabric;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ItemStackListFactory {
    private static final ItemStackListFactory instance = new ItemStackListFactory();

    private ItemStackListFactory() {
    }

    public static ItemStackListFactory getInstance() {
        return instance;
    }

    public List<ItemStack> create() {
        List<ItemStack> itemList = new ArrayList<>();
        Set<String> itemNameSet = new HashSet<>();
        ItemGroup[] var3 = ItemGroup.GROUPS;

        for (ItemGroup itemGroup : var3) {
            if (itemGroup != ItemGroup.HOTBAR && itemGroup != ItemGroup.INVENTORY) {
                DefaultedList<ItemStack> creativeTabItemStacks = DefaultedList.of();

                try {
                    itemGroup.appendStacks(creativeTabItemStacks);
                } catch (LinkageError | RuntimeException var10) {
                    ItemRenderFabric.logger.error("Item Group crashed while getting items.Some items from this group will be missing from the ingredient list. {}", itemGroup, var10);
                }

                for (ItemStack creativeTabItemStack : creativeTabItemStacks) {
                    if (creativeTabItemStack.isEmpty()) {
                        ItemRenderFabric.logger.error("Found an empty itemStack from creative tab: {}", itemGroup);
                    } else {
                        this.addItemStack(creativeTabItemStack, itemList, itemNameSet);
                    }
                }
            }
        }

        for (Item item : Registry.ITEM) {
            if (item.getGroup() == null) {
                ItemStack nogroupStack = new ItemStack(item);
                this.addItemStack(nogroupStack, itemList, itemNameSet);
            }
        }

        return itemList;
    }

    private void addItemStack(ItemStack stack, List<ItemStack> itemList, Set<String> itemNameSet) {
        if (stack.getItem() != Items.PLAYER_HEAD) {
            String itemKey;
            try {
                itemKey = Helper.getRegName(stack.getItem());
            } catch (LinkageError | RuntimeException var7) {
                String stackInfo = this.getItemStackInfo(stack);
                ItemRenderFabric.logger.error("Couldn't get unique name for itemStack {}", stackInfo, var7);
                return;
            }

            if (!itemNameSet.contains(itemKey)) {
                itemNameSet.add(itemKey);
                itemList.add(stack);
            }

        }
    }

    private String getItemStackInfo(ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        } else {
            Item item = itemStack.getItem();
            Identifier registryName = Helper.getRegNameI(item);
            String itemName = registryName.toString();

            NbtCompound nbt = itemStack.getTag();
            return nbt != null ? itemStack + " " + itemName + " nbt:" + nbt : itemStack + " " + itemName;
        }
    }
}
