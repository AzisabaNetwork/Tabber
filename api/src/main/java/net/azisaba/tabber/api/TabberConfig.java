package net.azisaba.tabber.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface TabberConfig {
    /**
     * Returns if the plugin is in debug mode.
     * @return true is plugin has unlocked debug mode; false otherwise
     */
    boolean isDebug();

    /**
     * Returns the server groups.
     * @return the server groups
     */
    @NotNull Map<@NotNull String, @NotNull List<@NotNull String>> getServerGroups();

    /**
     * Returns the update interval of the tab list order, in milliseconds.
     * @return the update interval
     */
    int getOrderUpdateInterval();
}
