package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.command.Command;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class UnloadCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "unload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Unloads the plugin.";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        TabberProvider.get().disable();
        TabberProvider.get().getCommandManager().registerCommand(new HelpCommand());
        TabberProvider.get().getCommandManager().registerCommand(new LoadCommand());
        sender.sendMessage(Component.text("Unloaded Tabber!", NamedTextColor.GREEN));
        sender.sendMessage(Component.text("To load Tabber again, use /tabber load", NamedTextColor.GREEN));
    }
}
