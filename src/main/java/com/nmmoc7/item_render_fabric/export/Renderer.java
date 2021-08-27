package com.nmmoc7.item_render_fabric.export;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3f;
import org.lwjgl.opengl.GL11;

import java.io.File;

public class Renderer {
    public Renderer() {
    }

    private static void RenderEntity(LivingEntity entity, FBOHelper fbo, boolean renderPlayer) {
        fbo.begin();
        Box aabb = entity.getBoundingBox();
        double minX = aabb.minX - entity.getX();
        double maxX = aabb.maxX - entity.getX();
        double minY = aabb.minY - entity.getY();
        double maxY = aabb.maxY - entity.getY();
        double minZ = aabb.minZ - entity.getZ();
        double maxZ = aabb.maxZ - entity.getZ();
        double minBound = Math.min(minX, Math.min(minY, minZ));
        double maxBound = Math.max(maxX, Math.max(maxY, maxZ));
        double boundLimit = Math.max(Math.abs(minBound), Math.abs(maxBound));
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.ortho(-boundLimit * 0.75D, boundLimit * 0.75D, boundLimit * 0.25D, -boundLimit * 1.25D, -100.0D, 100.0D);
        RenderSystem.matrixMode(5888);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0F, 0.0F, 50.0F);
        RenderSystem.scaled(-1.0D, 1.0D, 1.0D);

        MatrixStack transform = new MatrixStack();
        transform.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
        DiffuseLighting.enable();
        transform.multiply(Vec3f.POSITIVE_X.getRadialQuaternion((float)Math.asin(Math.tan(Math.toRadians(30.0D)))));
        transform.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(45.0F));
        EntityRenderDispatcher manager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        manager.setRenderShadows(false);
        VertexConsumerProvider.Immediate buffer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        manager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, transform, buffer, 15728880);
        buffer.draw();
        manager.setRenderShadows(true);
        RenderSystem.popMatrix();
        DiffuseLighting.disable();
        RenderSystem.matrixMode(5889);
        RenderSystem.popMatrix();
        fbo.end();
    }

    public static void renderItem(ItemStack itemStack, FBOHelper fbo, String filenameSuffix) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = minecraftClient.getItemRenderer();
        fbo.begin();
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, 16.0D, 0.0D, 16.0D, -150.0D, 150.0D);
        RenderSystem.matrixMode(5888);
        DiffuseLighting.enable();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
        RenderSystem.enableLighting();
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        itemRenderer.renderGuiItemIcon(itemStack, 0, 0);
        RenderSystem.disableLighting();
        DiffuseLighting.disable();
        RenderSystem.matrixMode(5889);
        GL11.glPopMatrix();
        fbo.end();
        fbo.saveToFile(new File(minecraftClient.runDirectory, String.format("export\\texture\\item_%s_%s.png", itemStack.getTranslationKey().replaceAll("[^A-Za-z0-9()\\[\\]]", ""), filenameSuffix)));
        fbo.restoreTexture();
    }

    public static void beginItem() {
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, 16.0D, 0.0D, 16.0D, -150.0D, 150.0D);
        RenderSystem.matrixMode(5888);
        DiffuseLighting.enable();
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableColorMaterial();
        RenderSystem.enableLighting();
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.pushMatrix();
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.alphaFunc(516, 0.1F);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void endItem() {
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();
        RenderSystem.popMatrix();
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.disableLighting();
        DiffuseLighting.disable();
        RenderSystem.matrixMode(5889);
        GL11.glPopMatrix();
    }

    public static String getItemBase64(ItemStack itemStack, FBOHelper fbo, ItemRenderer itemRenderer) {
        fbo.begin();
        itemRenderer.renderGuiItemIcon(itemStack, 0, 0);
        fbo.end();
        String base64 = fbo.getBase64(false);
        fbo.restoreTexture();
        return base64;
    }

    public static String getEntityBase64(EntityType<LivingEntity> entityType, FBOHelper fbo) {
        MinecraftClient mc = MinecraftClient.getInstance();
        LivingEntity entity = entityType.create(mc.world);
        if (entity == null) {
            return null;
        } else {
            RenderEntity(entity, fbo, false);
            String base64 = fbo.getBase64(true);
            fbo.restoreTexture();
            return base64;
        }
    }
}
