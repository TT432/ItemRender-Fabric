package com.nmmoc7.item_render_fabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nmmoc7.item_render_fabric.export.ExportMain;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class ExportAllCommand implements Command<FabricClientCommandSource> {
    public static final ExportAllCommand INSTANCE = new ExportAllCommand();
    public static final LiteralArgumentBuilder<FabricClientCommandSource> COMMAND =
            ClientCommandManager.literal("irexport")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(ClientCommandManager.literal("mods").executes(INSTANCE));

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        ExportMain.getInstance().DoExport();
        return 0;
    }
}
