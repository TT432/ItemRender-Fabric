//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nmmoc7.item_render_fabric.export;

import net.minecraft.item.ItemStack;

public class ItemData {
    public String name;
    public String englishName;
    public String registerName;
    public String TagList;
    public String CreativeTabName;
    public String type;
    public int maxStackSize;
    public int maxDurability;
    public String smallIcon;
    public String largeIcon;
    private transient ItemStack itemStack;

    public ItemData(ItemStack itemStack) {
        this.name = null;
        this.englishName = null;
        this.registerName = Helper.getRegName(itemStack.getItem());
        this.TagList = Helper.getTags(itemStack.getItem());
        if (this.TagList.equals("[]")) {
            this.TagList = null;
        }

        this.CreativeTabName = null;
        this.type = ExportMain.getInstance().getType(itemStack);
        this.maxStackSize = itemStack.getMaxCount();
        this.maxDurability = itemStack.getMaxDamage() + 1;
        this.smallIcon = ExportMain.getInstance().getSmallIcon(itemStack);
        this.largeIcon = ExportMain.getInstance().getLargeIcon(itemStack);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public void setCreativeName(String name) {
        this.CreativeTabName = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
