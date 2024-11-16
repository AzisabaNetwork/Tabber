package net.azisaba.tabber.api;

import net.azisaba.tabber.api.actor.TabberPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * This interface is used to interact with the platform-specific implementations of Tabber.
 */
public interface TabberPlatform {
    /**
     * Called when a player joins the proxy.
     * Also called when the plugin is reloaded, or the player is moved to another server.
     * @param player the player who joined
     */
    void onJoin(@NotNull TabberPlayer player);

    /**
     * Cancels all running tasks.
     */
    void cancelTasks();

    /**
     * Schedules a repeating task to update the order of the players.
     */
    void scheduleOrderUpdateTask();
}
