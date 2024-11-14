package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.Constants;
import net.azisaba.tabber.api.command.Command;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class VersionCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "version";
    }

    @Override
    public @NotNull String getDescription() {
        return "Show the version of this plugin";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        sender.sendMessage(Component.text("Tabber ", NamedTextColor.AQUA)
                .append(Component.text("v" + Constants.VERSION, NamedTextColor.YELLOW)));
    }
}
