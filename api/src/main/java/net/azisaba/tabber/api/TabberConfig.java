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
     * Returns if the plugin should isolate unlisted servers. (Group servers that are not listed in the configuration)
     * @return true if the plugin should isolate unlisted servers; false otherwise
     */
    boolean isolateUnlistedServers();

    /**
     * Returns the update interval of the tab list order, in milliseconds.
     * @return the update interval
     */
    int getOrderUpdateInterval();

    /**
     * Returns if the order should be the viewer's server first.
     * @return true if the order should be the viewer's server first; false otherwise
     */
    boolean isOrderViewerServerFirst();

    /**
     * Returns the order of the tab list.
     * @return the order
     */
    @NotNull List<@NotNull String> getOrder();
}
