package net.azisaba.tabber.api.impl.core;

import net.azisaba.tabber.api.core.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class RegistryImpl<K, V> implements Registry<K, V> {
    private static final RegistryImpl<?, ?> EMPTY = new RegistryImpl<>(Map.of());
    private final @NotNull Map<@NotNull K, @NotNull V> map;

    public RegistryImpl(@NotNull Map<@NotNull K, @NotNull V> map) {
        this.map = Map.copyOf(map);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull RegistryImpl<K, V> empty() {
        return (RegistryImpl<K, V>) EMPTY;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        return map.get(key);
    }

    @Override
    public @NotNull V getOrThrow(@NotNull K key) throws NoSuchElementException {
        V value = get(key);
        if (value == null) {
            throw new NoSuchElementException("No value found for key: " + key);
        }
        return value;
    }

    @Override
    public @NotNull Set<K> keys() {
        return map.keySet();
    }

    @Override
    public @NotNull List<V> values() {
        return List.copyOf(map.values());
    }
}
