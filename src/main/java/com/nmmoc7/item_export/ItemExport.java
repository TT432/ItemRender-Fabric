package com.nmmoc7.item_export;

import com.nmmoc7.item_export.command.ExportCommand;
import com.nmmoc7.item_export.command.ExportSingleCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

public class ItemExport implements ModInitializer {
    @Override
    public void onInitialize() {
        ClientCommandManager.DISPATCHER.register(ExportCommand.COMMAND);
        ClientCommandManager.DISPATCHER.register(ExportSingleCommand.COMMAND);
    }
}
