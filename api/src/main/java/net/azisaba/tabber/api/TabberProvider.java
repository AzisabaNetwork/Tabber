package net.azisaba.tabber.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class TabberProvider {
    private static Tabber instance;

    public static @NotNull Tabber get() {
        if (instance == null) {
            throw new IllegalStateException("TabberProvider instance is not set");
        }
        return instance;
    }

    @ApiStatus.Internal
    public static void setInstance(@NotNull Tabber instance) {
        if (TabberProvider.instance != null) {
            throw new IllegalStateException("Cannot redefine singleton TabberProvider instance");
        }
        TabberProvider.instance = instance;
    }

    private TabberProvider() {
    }
}
