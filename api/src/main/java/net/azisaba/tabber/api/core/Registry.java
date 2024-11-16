package net.azisaba.tabber.api.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public interface Registry<K, V> {
    /**
     * Returns the value associated with the key.
     * @param key the key
     * @return the value or null if not found
     */
    @Nullable V get(@NotNull K key);

    /**
     * Returns the value associated with the key.
     * @param key the key
     * @return the value or throws an exception if not found
     */
    @NotNull V getOrThrow(@NotNull K key) throws NoSuchElementException;

    /**
     * Returns all keys.
     * @return all keys
     */
    @NotNull Set<K> keys();

    /**
     * Returns the all values.
     * @return all values
     */
    @NotNull List<V> values();
}
