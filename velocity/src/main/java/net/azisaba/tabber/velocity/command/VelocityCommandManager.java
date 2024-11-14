package net.azisaba.tabber.velocity.command;

import net.azisaba.tabber.api.command.CommandManager;
import net.azisaba.tabber.api.command.Command;
import net.azisaba.tabber.velocity.VelocityTabber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.List;

public class VelocityCommandManager implements CommandManager {
    private final @NotNull VelocityTabber tabber;

    public VelocityCommandManager(@NotNull VelocityTabber tabber) {
        this.tabber = tabber;
    }

    @Override
    public void registerCommand(@NotNull Command command) {
        tabber.getPlugin().getCommand().registerCommand(command);
    }

    @Override
    public void unregisterAllCommands() {
        tabber.getPlugin().getCommand().unregisterAllCommands();
    }

    @Override
    @Unmodifiable
    public @NotNull List<@NotNull Command> getRegisteredCommands() {
        return Collections.unmodifiableList(tabber.getPlugin().getCommand().getCommands());
    }
}
