package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.command.Command;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Reloads the plugin.";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        TabberProvider.get().disable();
        TabberProvider.get().enable();
        sender.sendMessage(Component.text("Reloaded Tabber!", NamedTextColor.GREEN));
    }
}
