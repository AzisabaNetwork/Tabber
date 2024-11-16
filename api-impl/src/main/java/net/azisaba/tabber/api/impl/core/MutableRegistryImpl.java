package net.azisaba.tabber.api.impl.core;

import net.azisaba.tabber.api.core.MutableRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MutableRegistryImpl<K, V> implements MutableRegistry<K, V> {
    private final @NotNull Map<@NotNull K, @NotNull V> map;

    public MutableRegistryImpl(@NotNull Map<@NotNull K, @NotNull V> map) {
        this.map = new ConcurrentHashMap<>(map);
    }

    public MutableRegistryImpl() {
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void register(@NotNull K key, @NotNull V value) {
        map.put(key, value);
    }

    @Override
    public void unregister(@NotNull K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
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
