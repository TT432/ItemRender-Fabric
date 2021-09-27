package com.nmmoc7.item_export.exporter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.UUID;

public class Base64Helper {
    static FBOHelper small = new FBOHelper(32);
    static FBOHelper large = new FBOHelper(128);

    public static String getSmall(ItemStack itemStack) {
        return getItemBase64(itemStack, small);
    }

    public static String getLarge(ItemStack itemStack) {
        return getItemBase64(itemStack, large);
    }

    public static String getItemBase64(ItemStack stack, FBOHelper fbo) {
        fbo.clear();
        fbo.begin();
        renderItem(stack, MinecraftClient.getInstance().getItemRenderer());
        fbo.end();
        try (NativeImage image = dumpFrom(fbo.frame)) {
            return base64(image);
        } finally {
            fbo.unbindTexture();
        }
    }

    public static NativeImage dumpFrom(Framebuffer frame) {
        NativeImage img = new NativeImage(frame.textureWidth, frame.textureHeight, true);
        RenderSystem.bindTexture(frame.getColorAttachment());
        img.loadFromTextureImage(0, true);
        img.mirrorVertically();
        return img;
    }

    public static void renderItem(ItemStack itemStack, ItemRenderer itemRenderer) {
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0D, 16D, 16D, 0D, -150D, 150D);
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        DiffuseLighting.enableGuiDepthLighting();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
        RenderSystem.enableLighting();
        //RenderSystem.translated(8, 8, 0);
        // 神秘数字？
        RenderSystem.translated(0.0001, 0.0001, 0);
        itemRenderer.renderGuiItemIcon(itemStack, 0, 0);
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.popMatrix();
    }

    public static String base64(NativeImage img) {
        try {
            return Base64.getEncoder().encodeToString(img.getBytes());
        } catch (Exception e) {
            return "";
        } finally {
            img.close();
        }
    }
}
