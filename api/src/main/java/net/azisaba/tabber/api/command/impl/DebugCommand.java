package net.azisaba.tabber.api.command.impl;

import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.Command;
import net.azisaba.tabber.api.scoreboard.Team;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DebugCommand implements Command {
    private static final Map<String, BiConsumer<Audience, String[]>> debugCommands = Map.ofEntries(
            Map.entry("team", (sender, args) -> {
                TabberPlayer player = TabberProvider.get().getPlayer(args[0]).orElseThrow();
                for (@NotNull Team team : player.getScoreboard().getTeams()) {
                    sender.sendMessage(Component.text("Team: " + team.getName(), NamedTextColor.YELLOW));
                    sender.sendMessage(Component.text("  Display Name: " + team.getDisplayName(), NamedTextColor.YELLOW));
                    sender.sendMessage(Component.text("  Prefix: " + team.getPrefix(), NamedTextColor.YELLOW));
                    sender.sendMessage(Component.text("  Suffix: " + team.getSuffix(), NamedTextColor.YELLOW));
                    sender.sendMessage(Component.text("  Entries: " + team.getEntries(), NamedTextColor.YELLOW));
                }
            })
    );

    @Override
    public @NotNull String getName() {
        return "debug";
    }

    @Override
    public @NotNull String getDescription() {
        return "Debugging util";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /tabber debug <subcommand>", NamedTextColor.RED));
            return;
        }
        BiConsumer<Audience, String[]> consumer = debugCommands.get(args[0]);
        if (consumer == null) {
            sender.sendMessage(Component.text("Unknown subcommand: " + args[0], NamedTextColor.RED));
            return;
        }
        consumer.accept(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public @NotNull List<@NotNull String> getSuggestions(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            return List.copyOf(debugCommands.keySet());
        }
        if (args.length == 1) {
            return List.copyOf(debugCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList()));
        }
        return List.of();
    }
}
