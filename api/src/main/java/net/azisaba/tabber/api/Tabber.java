package net.azisaba.tabber.api;

import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.CommandManager;
import net.azisaba.tabber.api.command.impl.*;
import net.azisaba.tabber.api.event.EventBus;
import net.azisaba.tabber.api.event.TabberLoadEvent;
import net.azisaba.tabber.api.event.TabberUnloadEvent;
import net.azisaba.tabber.api.function.FunctionManager;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Tabber {
    /**
     * Returns the platform-specific implementation of Tabber.
     */
    @NotNull TabberPlatform getPlatform();

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
     * Returns the function manager which holds the function data for evaluating expressions using cel-java.
     * @return the function manager
     */
    @NotNull FunctionManager getFunctionManager();

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
        getFunctionManager().load();
        getCommandManager().registerCommand(new VersionCommand());
        getCommandManager().registerCommand(new HelpCommand());
        getCommandManager().registerCommand(new ReloadCommand());
        getCommandManager().registerCommand(new DebugCommand());
        getCommandManager().registerCommand(new EvalCommand());
        for (@NotNull TabberPlayer player : getOnlinePlayers()) {
            getPlatform().onJoin(player);
        }
        EventBus.INSTANCE.callEvent(new TabberLoadEvent());
    }

    /**
     * Disables the features of the plugin.
     */
    default void disable() {
        EventBus.INSTANCE.callEvent(new TabberUnloadEvent());
        getPlatform().cancelTasks();
        getCommandManager().unregisterAllCommands();
        getFunctionManager().unload();
    }
}
