package com.github.unjoinable.skyblock.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.monitoring.TickMonitor;
import net.minestom.server.utils.MathUtils;

import java.util.concurrent.atomic.AtomicReference;

public class ServerResourcesCommand extends Command {

    public ServerResourcesCommand() {
        super("serverResources");
        AtomicReference<TickMonitor> lastTick = new AtomicReference<>();
        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, event ->
                lastTick.set(event.getTickMonitor()));

        addSyntax((sender, context) -> {
            Player player = ((Player) sender);
            final Runtime runtime = Runtime.getRuntime();
            final TickMonitor tickMonitor = lastTick.get();
            final long ramUsage = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;

            final double tickTime = MathUtils.round(tickMonitor.getTickTime(), 2);
            final double tps = MathUtils.round(1000/tickTime, 0);

            final Component debug = Component.text("[DEBUG] ", NamedTextColor.RED);
            final Component info = Component.empty()
                    .append(debug)
                    .append(Component.text("RAM USAGE: " + ramUsage + " MB", NamedTextColor.GRAY)
                            .append(Component.newline())
                            .append(debug)
                            .append(Component.text("TICK TIME: " + tickTime + "ms", NamedTextColor.GRAY))
                            .append(Component.newline())
                            .append(debug)
                            .append(Component.text("TPS: " + tps, NamedTextColor.GRAY)));
            player.sendMessage(info);
        });
    }
}
