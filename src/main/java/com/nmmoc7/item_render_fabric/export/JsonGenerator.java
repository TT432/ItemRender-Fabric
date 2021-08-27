package com.nmmoc7.item_render_fabric.export;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public final class JsonGenerator {
    public final Gson gson = (new GsonBuilder()).disableHtmlEscaping().create();
    private static final JsonGenerator instance = new JsonGenerator();

    private JsonGenerator() {
    }

    public static JsonGenerator getInstance() {
        return instance;
    }

    public void exportItem(String modid, Collection<ItemData> item) {
        File fileDir = new File("export\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            StringBuilder builder = new StringBuilder("export\\");
            File export = new File(String.format(builder.append(modid).append("_item.json").toString(), modid.replaceAll("[^A-Za-z0-9()\\[\\]]", "")));
            if (!export.getParentFile().exists()) {
                export.getParentFile().mkdirs();
            }

            if (!export.exists()) {
                export.createNewFile();
            }

            PrintWriter pw = new PrintWriter(export, "UTF-8");

            for (ItemData data : item) {
                pw.println(this.gson.toJson(data));
            }

            pw.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }

    public void exportEntity(String modid, Collection<MobData> mob) {
        File fileDir = new File("export\\");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            StringBuilder builder = new StringBuilder("export\\");
            File export = new File(String.format(builder.append(modid).append("_entity.json").toString(), modid.replaceAll("[^A-Za-z0-9()\\[\\]]", "")));
            if (!export.getParentFile().exists()) {
                export.getParentFile().mkdirs();
            }

            if (!export.exists()) {
                export.createNewFile();
            }

            PrintWriter pw = new PrintWriter(export, "UTF-8");

            for (MobData data : mob) {
                pw.println(this.gson.toJson(data));
            }

            pw.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }
    }
}
