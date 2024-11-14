package net.azisaba.tabber.api;

import net.azisaba.tabber.api.command.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface CommandManager {
    /**
     * Registers a /tabber command.
     * @param command the command to register
     */
    void registerCommand(@NotNull Command command);

    /**
     * Unregisters all commands registered by the plugin.
     */
    void unregisterAllCommands();

    /**
     * Return all registered commands. This list is unmodifiable.
     * @return all registered commands
     */
    @Unmodifiable
    @NotNull List<@NotNull Command> getRegisteredCommands();
}
