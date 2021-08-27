//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nmmoc7.item_render_fabric.export;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class MobData {
    public String name;
    public String Englishname;
    public String mod;
    public String registerName;
    public String Icon;
    private transient EntityType<LivingEntity> mob;

    public MobData(EntityType<LivingEntity> entity) {
        this.name = null;
        this.Englishname = null;
        this.mod = Helper.getRegNameI(entity).getNamespace();
        this.registerName = Helper.getRegName(entity);
        this.Icon = ExportMain.getInstance().getEntityIcon(entity);
        this.mob = entity;
    }

    public EntityType<LivingEntity> getMob() {
        return this.mob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnglishname(String name) {
        this.Englishname = name;
    }
}
