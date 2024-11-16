package net.azisaba.tabber.api.core;

import org.jetbrains.annotations.NotNull;

public interface MutableRegistry<K, V> extends Registry<K, V> {
    void register(@NotNull K key, @NotNull V value);

    void unregister(@NotNull K key);

    void clear();
}
