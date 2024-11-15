package net.azisaba.tabber.api.event;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public final class RegisteredEvent<T> {
    private final @NotNull Object plugin;
    private final @NotNull Class<T> clazz;
    private final int priority;
    private final @NotNull Consumer<T> action;

    public RegisteredEvent(@NotNull Object plugin, @NotNull Class<T> clazz, int priority, @NotNull Consumer<T> action) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.clazz = Objects.requireNonNull(clazz, "clazz");
        this.priority = priority;
        this.action = Objects.requireNonNull(action, "action");
    }

    public @NotNull Object getPlugin() {
        return plugin;
    }

    public @NotNull Class<T> getClazz() {
        return clazz;
    }

    public int getPriority() {
        return priority;
    }

    public @NotNull Consumer<@NotNull T> getAction() {
        return action;
    }
}
