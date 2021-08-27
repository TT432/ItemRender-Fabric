package com.nmmoc7.item_render_fabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.item.Item;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class ExportCommand implements Command<ServerCommandSource> {
    public static final ExportCommand INSTANCE = new ExportCommand();
    public static final LiteralArgumentBuilder<ServerCommandSource> COMMAND =
            CommandManager.literal("irexport")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("mod", StringArgumentType.word()))
                    .executes(INSTANCE);

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // todo
        Iterator<Item> items = Registry.ITEM.iterator();

        while (items.hasNext()) {

        }
        return 0;
    }
}
