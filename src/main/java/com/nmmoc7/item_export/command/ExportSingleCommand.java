package com.nmmoc7.item_export.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nmmoc7.item_export.exporter.ExportHandler;
import com.nmmoc7.item_export.exporter.NameHelper;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;

public class ExportSingleCommand implements Command<FabricClientCommandSource> {
    public static final ExportSingleCommand INSTANCE = new ExportSingleCommand();
    public static final LiteralArgumentBuilder<FabricClientCommandSource> COMMAND =
            CommandHelper.getModCommand()
                    .then(ClientCommandManager.literal("mod")
                            .then(ClientCommandManager.argument("mod", StringArgumentType.word())
                                    .executes(INSTANCE)));

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        ExportHandler.INSTANCE.scan();
        String modId = context.getArgument("mod", String.class);
        NameHelper.ModItems temp = NameHelper.INSTANCE.MOD_ITEMS.get(modId);

        if (temp != null) {
            ExportHandler.INSTANCE.exportItem(modId, temp);
        }
        else {
            context.getSource().sendError(new LiteralText("[Item Export] Mod 不存在"));
        }
        return 0;
    }
}
