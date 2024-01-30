package com.nmmoc7.item_export.exporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ExportHandler {
    public static final ExportHandler INSTANCE = new ExportHandler();

    public boolean scan = false;

    public void scan() {
        if (!scan) {
            NameHelper.INSTANCE.scan();
            scan = true;
        }
    }

    public void doExport() {
        scan();
        NameHelper.INSTANCE.MOD_ITEMS.forEach(INSTANCE::exportItem);
    }

    public void exportItem(String modId, NameHelper.ModItems modItems) {
        File fileDir = new File("export");

        if (!fileDir.exists())
            fileDir.mkdirs();

        try {
            if (!fileDir.isDirectory())
                throw new IOException("Cannot create export directory: File exists");

            StringBuilder builder = new StringBuilder(modId);
            File export = new File(fileDir, String.format(builder.append("_item.json").toString(), modId.replaceAll("[^A-Za-z0-9()\\[\\]]", "")));

            if (!export.exists())
                export.createNewFile();

            PrintWriter pw = new PrintWriter(export, "UTF-8");

            for (FormatJson json: modItems.map.values()) {
                pw.println(json.toJson());
            }

            pw.close();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

    }
}
