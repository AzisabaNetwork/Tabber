package net.azisaba.tabber.api;

import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.CommandManager;
import net.azisaba.tabber.api.command.impl.DebugCommand;
import net.azisaba.tabber.api.command.impl.HelpCommand;
import net.azisaba.tabber.api.command.impl.ReloadCommand;
import net.azisaba.tabber.api.command.impl.VersionCommand;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Tabber {
    /**
     * Reloads the configuration of the plugin.
     */
    void reloadConfig();

    /**
     * Returns the configuration of the plugin. Configuration is read-only.
     * @return the configuration
     */
    @NotNull TabberConfig getConfig();

    /**
     * Returns the logger of the plugin.
     * @return the logger
     */
    @NotNull Logger getLogger();

    /**
     * Returns the command manager.
     * @return the command manager
     * @see CommandManager
     */
    @NotNull CommandManager getCommandManager();

    /**
     * Returns the placeholder manager which holds the placeholder data.
     * @return the placeholder manager
     */
    @NotNull PlaceholderManager getPlaceholderManager();

    /**
     * Returns the player object by their Minecraft username.
     * @param username the name of the player
     * @return the player object
     */
    @NotNull Optional<@NotNull TabberPlayer> getPlayer(@NotNull String username);

    /**
     * Returns the player object by their Minecraft UUID.
     * @param uuid the UUID of the player
     * @return the player object
     */
    @NotNull Optional<@NotNull TabberPlayer> getPlayer(@NotNull UUID uuid);

    /**
     * Returns the list of online players.
     * @return the list of online players
     */
    @NotNull List<@NotNull TabberPlayer> getOnlinePlayers();

    /**
     * Enables the features of the plugin.
     */
    default void enable() {
        reloadConfig();
        getCommandManager().registerCommand(new VersionCommand());
        getCommandManager().registerCommand(new HelpCommand());
        getCommandManager().registerCommand(new ReloadCommand());
        getCommandManager().registerCommand(new DebugCommand());
    }

    /**
     * Disables the features of the plugin.
     */
    default void disable() {
        getCommandManager().unregisterAllCommands();
    }
}
