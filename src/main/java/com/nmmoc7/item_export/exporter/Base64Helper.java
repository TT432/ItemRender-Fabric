package com.nmmoc7.item_export.exporter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;

public class Base64Helper {
    public static final FBOHelper fboSmall = new FBOHelper(32);
    public static final FBOHelper fboLarge = new FBOHelper(128);

    public static String getSmall(ItemStack itemStack) {
        return getItemBase64(itemStack, fboSmall, MinecraftClient.getInstance().getItemRenderer());
    }

    public static String getLarge(ItemStack itemStack) {
        return getItemBase64(itemStack, fboLarge, MinecraftClient.getInstance().getItemRenderer());
    }

    public static String getItemBase64(ItemStack itemStack, FBOHelper fbo, ItemRenderer itemRenderer) {
        fbo.begin();
        itemRenderer.renderGuiItemIcon(itemStack, 0, 0);
        fbo.end();
        String base64 = fbo.getBase64(false);
        fbo.restoreTexture();
        return base64;
    }
}
