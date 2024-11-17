package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.Command;
import net.azisaba.tabber.api.placeholder.Placeholder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ParseCommand implements Command {
    @Override
    public @NotNull String getName() {
        return "parse";
    }

    @Override
    public @NotNull String getDescription() {
        return "Replaces the placeholders in the text";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /tab parse <player> <text>").color(NamedTextColor.RED));
            return;
        }
        sender.sendMessage(Component.text("Replacing placeholder " + args[1] + " for player " + args[0] + ":", NamedTextColor.YELLOW));
        TabberPlayer player = TabberProvider.get().getPlayer(args[0]).orElseThrow();
        String text = TabberProvider.get().getPlaceholderManager().getPlaceholderByIdentifier(args[1]).orElseThrow().replace(args[1], player);
        sender.sendMessage(Component.text(text));
    }

    @Override
    public @NotNull List<@NotNull String> getSuggestions(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return TabberProvider.get()
                    .getOnlinePlayers()
                    .stream()
                    .map(TabberPlayer::getUsername)
                    .filter(p -> p.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        if (args.length == 2) {
            return TabberProvider.get()
                    .getPlaceholderManager()
                    .getKnownPlaceholders()
                    .stream()
                    .map(Placeholder::getIdentifier)
                    .filter(p -> p.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return Command.super.getSuggestions(sender, args);
    }
}
