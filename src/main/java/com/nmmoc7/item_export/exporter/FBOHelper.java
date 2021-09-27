package com.nmmoc7.item_export.exporter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL11;

public final class FBOHelper {

    Framebuffer frame;

    private int size = 128;

    public FBOHelper(int textureSize) {
        this.size = textureSize;
        init();
    }

    public void resize(int newSize) {
        this.frame.resize(this.size = newSize, newSize, true);
    }

    public void init() {
        if (this.frame == null) {
            this.frame = new Framebuffer(this.size, this.size, true, MinecraftClient.IS_SYSTEM_MAC);
        }
    }

    public void begin() {
        this.frame.beginWrite(true);
        RenderSystem.enableTexture();
        this.frame.beginRead();

        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();

        RenderSystem.clearColor(1F, 1F, 1F, 1F);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableLighting();
        RenderSystem.enableRescaleNormal();
    }

    public void end() {
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.disableDepthTest();

        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.popMatrix();

        this.frame.endRead();
    }

    public void clear() {
        RenderSystem.clearColor(1F, 1F, 1F, 1F);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, true);
    }

    public void bindTexture() {
        this.frame.beginRead();
    }

    public void unbindTexture() {
        this.frame.endRead();
    }

}
