package com.nmmoc7.item_render_fabric;

import com.nmmoc7.item_render_fabric.command.ExportAllCommand;
import com.nmmoc7.item_render_fabric.command.ExportCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemRenderFabric implements ModInitializer {
    public static final Logger logger = LogManager.getLogger();

    @Override
    public void onInitialize() {
        // CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
        //     dispatcher.register(ExportCommand.COMMAND);
        //     dispatcher.register(ExportAllCommand.COMMAND);
        // });

        ClientCommandManager.DISPATCHER.register(ExportAllCommand.COMMAND);
    }
}
