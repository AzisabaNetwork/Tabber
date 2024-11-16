package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.command.Command;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class LoadCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "load";
    }

    @Override
    public @NotNull String getDescription() {
        return "Loads the plugin.";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        TabberProvider.get().getCommandManager().unregisterAllCommands();
        TabberProvider.get().enable();
        sender.sendMessage(Component.text("Loaded Tabber!", NamedTextColor.GREEN));
    }
}
