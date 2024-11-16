package net.azisaba.tabber.api.config;

import net.azisaba.tabber.api.order.OrderData;
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
     * Returns the spy servers which are not listed in the tab list of players in other servers,
     * but can be seen by the players in the spy servers.
     * @return the spy servers
     */
    @NotNull List<@NotNull String> getSpyServers();

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
     * Returns the order of the tab list.
     * @return the order
     */
    @NotNull List<@NotNull OrderData> getOrder();
}
