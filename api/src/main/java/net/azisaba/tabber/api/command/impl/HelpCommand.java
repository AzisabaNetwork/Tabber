package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.command.Command;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "help";
    }

    @Override
    public @NotNull String getDescription() {
        return "Shows what you're looking at right now";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            for (@NotNull Command command : TabberProvider.get().getCommandManager().getRegisteredCommands()) {
                sender.sendMessage(Component.text("/tabber " + command.getName(), NamedTextColor.AQUA)
                        .append(Component.text(" - ", NamedTextColor.GRAY))
                        .append(Component.text(command.getDescription(), NamedTextColor.YELLOW)));
            }
            return;
        }
        for (@NotNull Command command : TabberProvider.get().getCommandManager().getRegisteredCommands()) {
            if (command.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(Component.text("/tabber " + command.getName(), NamedTextColor.AQUA)
                        .append(Component.text(" - ", NamedTextColor.GRAY))
                        .append(Component.text(command.getDescription(), NamedTextColor.YELLOW)));
                return;
            }
        }
        sender.sendMessage(Component.text("No help for " + args[0] + " :(", NamedTextColor.RED));
    }

    @Override
    public @NotNull List<@NotNull String> getSuggestions(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            return TabberProvider.get().getCommandManager().getRegisteredCommands().stream()
                    .map(Command::getName)
                    .collect(Collectors.toList());
        }
        return TabberProvider.get().getCommandManager().getRegisteredCommands().stream()
                .map(Command::getName)
                .filter(name -> name.startsWith(args[0]))
                .collect(Collectors.toList());
    }
}
