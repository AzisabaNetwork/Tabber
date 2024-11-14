package net.azisaba.tabber.api.command;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Command {
    @NotNull String getName();

    @NotNull String getDescription();

    void execute(@NotNull Audience sender, @NotNull String @NotNull [] args);

    default @NotNull List<@NotNull String> getSuggestions(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        return List.of();
    }
}
