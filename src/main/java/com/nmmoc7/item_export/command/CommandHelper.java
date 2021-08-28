package com.nmmoc7.item_export.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class CommandHelper {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getModCommand() {
        return ClientCommandManager.literal("itemExport");
    }
}
