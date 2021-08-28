package com.nmmoc7.item_export.exporter;

import com.google.gson.JsonObject;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class FormatJson {
    String zhName;
    String enName = "";
    String regName;
    Type type;
    int maxStackSize;
    int maxDurability;
    String tags;
    String smallIcon;
    String largeIcon;

    public FormatJson(ItemStack item) {
        regName = NameHelper.getRegName(item.getItem());
        type = item.getItem() instanceof BlockItem ? Type.Block : Type.Item;
        maxStackSize = item.getMaxCount();
        maxDurability = item.getMaxDamage();
        smallIcon = Base64Helper.getSmall(item);
        largeIcon = Base64Helper.getLarge(item);
        tags = NameHelper.getTags(item.getItem()).toString();
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("name", zhName);
        result.addProperty("englishName", enName);
        result.addProperty("registerName", regName);
        result.addProperty("type", type.name());
        result.addProperty("OredictList", tags);
        result.addProperty("maxStacksSize", maxStackSize);
        result.addProperty("maxDurability", maxDurability);
        result.addProperty("smallIcon", smallIcon);
        result.addProperty("largeIcon", largeIcon);
        return result;
    }

    public enum Type {
        Item,
        Block
    }
}
