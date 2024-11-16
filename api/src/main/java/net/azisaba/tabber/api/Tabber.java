package net.azisaba.tabber.api;

import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.CommandManager;
import net.azisaba.tabber.api.command.impl.*;
import net.azisaba.tabber.api.config.TabberConfig;
import net.azisaba.tabber.api.core.MutableRegistry;
import net.azisaba.tabber.api.event.EventBus;
import net.azisaba.tabber.api.event.TabberLoadEvent;
import net.azisaba.tabber.api.event.TabberPreLoadEvent;
import net.azisaba.tabber.api.event.TabberUnloadEvent;
import net.azisaba.tabber.api.function.FunctionManager;
import net.azisaba.tabber.api.order.OrderData;
import net.azisaba.tabber.api.order.OrderType;
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
     * Returns the order type registry.
     * @return the order type registry
     */
    @NotNull MutableRegistry<String, OrderType> getOrderTypeRegistry();

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
        Logger.getCurrentLogger().info("Enabling Tabber...");
        Logger.getCurrentLogger().info("TAB's scoreboard-teams feature is not compatible with Tabber. If you're using TAB, please disable the scoreboard-teams feature.");
        getOrderTypeRegistry().register("A_TO_Z", OrderType.ASCENDING);
        getOrderTypeRegistry().register("ASC", OrderType.ASCENDING);
        getOrderTypeRegistry().register("ASCENDING", OrderType.ASCENDING);
        getOrderTypeRegistry().register("Z_TO_A", OrderType.DESCENDING);
        getOrderTypeRegistry().register("DESC", OrderType.DESCENDING);
        getOrderTypeRegistry().register("DESCENDING", OrderType.DESCENDING);
        getOrderTypeRegistry().register("LOW_TO_HIGH", OrderType.LOW_TO_HIGH);
        getOrderTypeRegistry().register("HIGH_TO_LOW", OrderType.HIGH_TO_LOW);
        getOrderTypeRegistry().register("LOW_TO_HIGH_INT", OrderType.LOW_TO_HIGH_INT);
        getOrderTypeRegistry().register("HIGH_TO_LOW_INT", OrderType.HIGH_TO_LOW_INT);
        EventBus.INSTANCE.callEvent(new TabberPreLoadEvent());
        getFunctionManager().load();
        try {
            reloadConfig();
        } catch (Exception e) {
            Logger.getCurrentLogger().error("Failed to load the configuration", e);
        }
        getCommandManager().registerCommand(new VersionCommand());
        getCommandManager().registerCommand(new HelpCommand());
        getCommandManager().registerCommand(new ReloadCommand());
        getCommandManager().registerCommand(new DebugCommand());
        getCommandManager().registerCommand(new EvalCommand());
        getCommandManager().registerCommand(new UnloadCommand());
        for (@NotNull TabberPlayer player : getOnlinePlayers()) {
            try {
                getPlatform().onJoin(player);
            } catch (Exception e) {
                Logger.getCurrentLogger().error("Failed to execute onJoin hook for player {}", player.getUsername(), e);
            }
        }
        getPlatform().scheduleOrderUpdateTask();
        EventBus.INSTANCE.callEvent(new TabberLoadEvent());
        Logger.getCurrentLogger().info("Enabled Tabber v" + Constants.VERSION);
    }

    /**
     * Disables the features of the plugin.
     */
    default void disable() {
        Logger.getCurrentLogger().info("Disabling Tabber...");
        EventBus.INSTANCE.callEvent(new TabberUnloadEvent());
        getPlatform().cancelTasks();
        for (@NotNull TabberPlayer player : getOnlinePlayers()) {
            player.unregisterScoreboard();
        }
        getCommandManager().unregisterAllCommands();
        getFunctionManager().unload();
        OrderData.CEL_COMPILER.invalidate();
        OrderData.CEL_RUNTIME.invalidate();
        getOrderTypeRegistry().clear();
        Logger.getCurrentLogger().info("Disabled Tabber v" + Constants.VERSION);
    }
}
