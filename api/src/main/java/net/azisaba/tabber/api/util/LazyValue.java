package net.azisaba.tabber.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a lazy-loaded value.
 * @param <T> the type of the value
 */
public class LazyValue<T> {
    private final Object lock = new Object();
    private final LazyLoader<T> loader;

    private volatile T value;

    public LazyValue(@NotNull LazyLoader<T> loader) {
        this.loader = Objects.requireNonNull(loader);
    }

    public @NotNull T get() {
        T current = value;
        if (current == null) {
            synchronized (lock) {
                current = value;
                if (current == null) {
                    current = Objects.requireNonNull(loader.load(), "loader returned null");
                    value = current;
                }
            }
        }
        return current;
    }

    public void invalidate() {
        synchronized (lock) {
            value = null;
        }
    }

    public interface LazyLoader<T> {
        @NotNull
        T load();
    }
}
