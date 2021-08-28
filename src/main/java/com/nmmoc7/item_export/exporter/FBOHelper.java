package com.nmmoc7.item_export.exporter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.util.GlAllocationUtils;
import org.apache.commons.codec.binary.Base64;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class FBOHelper {
    private int renderTextureSize = 128;
    private int framebufferID = -1;
    private int textureID = -1;
    private IntBuffer lastViewport;
    private int lastTexture;
    private int lastFramebuffer;

    public FBOHelper(int textureSize) {
        this.renderTextureSize = textureSize;
        this.createFramebuffer();
    }

    void begin() {
        this.lastFramebuffer = GL11.glGetInteger(36006);
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.framebufferID);
        this.lastViewport = GlAllocationUtils.allocateByteBuffer(64).asIntBuffer();
        GL11.glGetIntegerv(2978, this.lastViewport);
        GL11.glViewport(0, 0, this.renderTextureSize, this.renderTextureSize);
        RenderSystem.matrixMode(5888);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        this.lastTexture = GL11.glGetInteger(32873);
        RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        RenderSystem.clear(16640, false);
        GL11.glCullFace(1028);
        RenderSystem.enableDepthTest();
        RenderSystem.enableLighting();
        RenderSystem.enableRescaleNormal();
    }

    void end() {
        GL11.glCullFace(1029);
        RenderSystem.disableDepthTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.matrixMode(5888);
        RenderSystem.popMatrix();
        GL11.glViewport(this.lastViewport.get(0), this.lastViewport.get(1), this.lastViewport.get(2), this.lastViewport.get(3));
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.lastFramebuffer);
        RenderSystem.bindTexture(this.lastTexture);
    }

    void restoreTexture() {
        RenderSystem.bindTexture(this.lastTexture);
    }

    void saveToFile(File file) {
        RenderSystem.bindTexture(this.textureID);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
        int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
        IntBuffer texture = BufferUtils.createIntBuffer(width * height);
        GL11.glGetTexImage(3553, 0, 32993, 33639, texture);
        int[] texture_array = new int[width * height];
        texture.get(texture_array);
        BufferedImage image = new BufferedImage(this.renderTextureSize, this.renderTextureSize, 2);
        image.setRGB(0, 0, this.renderTextureSize, this.renderTextureSize, texture_array, 0, width);
        file.mkdirs();

        try {
            ImageIO.write(image, "png", file);
        } catch (Exception ignored) {
        }
    }

    String getBase64(boolean flip) {
        RenderSystem.bindTexture(this.textureID);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
        int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
        IntBuffer texture = BufferUtils.createIntBuffer(width * height);
        GL11.glGetTexImage(3553, 0, 32993, 33639, texture);
        int[] texture_array = new int[width * height];
        texture.get(texture_array);
        BufferedImage image = new BufferedImage(this.renderTextureSize, this.renderTextureSize, 2);
        image.setRGB(0, 0, this.renderTextureSize, this.renderTextureSize, texture_array, 0, width);
        if (flip) {
            image = this.getFlippedImage(image);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "PNG", out);
        } catch (IOException ignored) {
        }

        return Base64.encodeBase64String(out.toByteArray());
    }

    private BufferedImage getFlippedImage(BufferedImage bi) {
        BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
        AffineTransform tran = AffineTransform.getTranslateInstance(0.0D, (double)bi.getHeight());
        AffineTransform flip = AffineTransform.getScaleInstance(1.0D, -1.0D);
        tran.concatenate(flip);
        Graphics2D g = flipped.createGraphics();
        g.setTransform(tran);
        g.drawImage(bi, 0, 0, (ImageObserver)null);
        g.dispose();
        return flipped;
    }

    private void createFramebuffer() {
        this.framebufferID = EXTFramebufferObject.glGenFramebuffersEXT();
        this.textureID = GL11.glGenTextures();
        int currentFramebuffer = GL11.glGetInteger(36006);
        int currentTexture = GL11.glGetInteger(32873);
        EXTFramebufferObject.glBindFramebufferEXT(36160, this.framebufferID);
        GL11.glBindTexture(3553, this.textureID);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GL11.glTexImage2D(3553, 0, 6408, this.renderTextureSize, this.renderTextureSize, 0, 32993, 5121, (ByteBuffer)null);
        GL11.glBindTexture(3553, currentTexture);
        int depthbufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(36161, depthbufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT(36161, 6402, this.renderTextureSize, this.renderTextureSize);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, depthbufferID);
        EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, this.textureID, 0);
        EXTFramebufferObject.glBindFramebufferEXT(36160, currentFramebuffer);
    }
}
