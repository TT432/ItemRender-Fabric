//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nmmoc7.item_render_fabric.export;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public final class ExportMain {
    public FBOHelper fboSmall = new FBOHelper(32);
    public FBOHelper fboLarge = new FBOHelper(128);
    public FBOHelper fboEntity = new FBOHelper(200);
    private ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    private static final ExportMain instance = new ExportMain();

    private ExportMain() {
    }

    public static ExportMain getInstance() {
        return instance;
    }

    public String getType(ItemStack itemStack) {
        return Block.getBlockFromItem(itemStack.getItem()) != Blocks.AIR ? "Block" : "Item";
    }

    public String getSmallIcon(ItemStack itemStack) {
        return Renderer.getItemBase64(itemStack, this.fboSmall, this.itemRenderer);
    }

    public String getLargeIcon(ItemStack itemStack) {
        return Renderer.getItemBase64(itemStack, this.fboLarge, this.itemRenderer);
    }

    public String getEntityIcon(EntityType<LivingEntity> Entitymob) {
        return Renderer.getEntityBase64(Entitymob, this.fboEntity);
    }

    private String getItemOwner(ItemStack itemStack) {
        return Helper.getRegNameI(itemStack.getItem()).getNamespace();
    }

    private String getEntityOwner(EntityType<LivingEntity> entityEntityType) {
        Identifier registryName = Registry.ENTITY_TYPE.getId(entityEntityType);
        return registryName.getNamespace();
    }

    public void DoExport() {
        Multimap<String, ItemData> itemDataList = LinkedListMultimap.create();
        Multimap<String, MobData> mobDataList = LinkedListMultimap.create();
        MinecraftClient mc = MinecraftClient.getInstance();
        Renderer.beginItem();
        Iterator<ItemStack> var7 = ItemStackListFactory.getInstance().create().iterator();

        while(true) {
            ItemData itemData;
            String mod_id;
            do {
                if (!var7.hasNext()) {
                    Renderer.endItem();
                    Iterator var11 = Registry.ENTITY_TYPE.iterator();

                    while(true) {
                        EntityType entity;
                        do {
                            Entity entity_instance;
                            do {
                                if (!var11.hasNext()) {
                                    this.refreshLanguage(mc, "zh_cn");
                                    var11 = itemDataList.values().iterator();

                                    ItemData data;
                                    while(var11.hasNext()) {
                                        data = (ItemData)var11.next();
                                        data.setName(data.getItemStack().getName().getString());
                                        data.setCreativeName(this.getCreativeTabName(data));
                                    }

                                    var11 = mobDataList.values().iterator();

                                    MobData dataE;
                                    while(var11.hasNext()) {
                                        dataE = (MobData)var11.next();
                                        dataE.setName(dataE.getMob().getName().getString());
                                    }

                                    this.refreshLanguage(mc, "en_us");
                                    var11 = itemDataList.values().iterator();

                                    while(var11.hasNext()) {
                                        data = (ItemData)var11.next();
                                        data.setEnglishName(data.getItemStack().getName().getString());
                                    }

                                    var11 = mobDataList.values().iterator();

                                    while(var11.hasNext()) {
                                        dataE = (MobData)var11.next();
                                        dataE.setEnglishname(dataE.getMob().getName().getString());
                                    }

                                    var11 = itemDataList.keySet().iterator();

                                    String modid;
                                    while(var11.hasNext()) {
                                        modid = (String)var11.next();
                                        JsonGenerator.getInstance().exportItem(modid, itemDataList.get(modid));
                                    }

                                    var11 = mobDataList.keySet().iterator();

                                    while(var11.hasNext()) {
                                        modid = (String)var11.next();
                                        JsonGenerator.getInstance().exportEntity(modid, mobDataList.get(modid));
                                    }

                                    return;
                                }

                                entity = (EntityType<?>)var11.next();
                                entity_instance = entity.create(mc.world);
                            } while(!(entity_instance instanceof LivingEntity));

                            mod_id = this.getEntityOwner(entity);
                        } while(mod_id.equalsIgnoreCase("minecraft"));

                        MobData mobData = new MobData(entity);
                        mobDataList.put(mod_id, mobData);
                    }
                }

                ItemStack itemStack = var7.next();
                itemData = new ItemData(itemStack);
                mod_id = this.getItemOwner(itemStack);
            } while(mod_id.equalsIgnoreCase("minecraft"));

            itemDataList.put(mod_id, itemData);
        }
    }

    private void refreshLanguage(MinecraftClient mc, String lang) {
        if (!mc.options.language.equals(lang)) {
            mc.getLanguageManager().setLanguage(new LanguageDefinition(lang, "", "", false));
            mc.options.language = lang;
            mc.getLanguageManager().reload(mc.getResourceManager());
        }

    }

    private String getCreativeTabName(ItemData data) {
        ItemGroup tab = data.getItemStack().getItem().getGroup();
        return tab != null ? I18n.translate(tab.getTranslationKey().getString()) : null;
    }
}
