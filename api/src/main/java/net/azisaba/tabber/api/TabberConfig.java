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

    @NotNull Map<@NotNull String, @NotNull List<@NotNull String>> getServerGroups();
}
