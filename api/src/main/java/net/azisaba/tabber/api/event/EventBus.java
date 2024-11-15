package net.azisaba.tabber.api.event;

import net.azisaba.tabber.api.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class EventBus {
    public static final EventBus INSTANCE = new EventBus();
    private final Map<Class<?>, List<RegisteredEvent<?>>> eventMap = new ConcurrentHashMap<>();

    public <T> void register(@NotNull Object plugin, @NotNull Class<T> clazz, @Range(from = -128, to = 128) int priority, @NotNull Consumer<@NotNull T> action) {
        // runtime check
        //noinspection ConstantValue
        if (priority < -128 || priority > 128) {
            throw new IllegalArgumentException("priority out of range");
        }
        RegisteredEvent<T> registeredEvent = new RegisteredEvent<>(plugin, clazz, priority, action);
        List<RegisteredEvent<?>> list = eventMap.computeIfAbsent(clazz, k -> Collections.synchronizedList(new ArrayList<>()));
        list.add(registeredEvent);
        list.sort(Comparator.comparingInt(RegisteredEvent::getPriority));
    }

    public void unregister(@NotNull Object plugin) {
        eventMap.forEach((clazz, list) -> list.removeIf(r -> r.getPlugin() == plugin));
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    public <T> @NotNull T callEvent(@NotNull T value) {
        List<RegisteredEvent<?>> list = eventMap.get(value.getClass());
        if (list == null) return value;
        for (RegisteredEvent<?> registeredEvent : list) {
            try {
                ((RegisteredEvent<T>) registeredEvent).getAction().accept(value);
            } catch (Throwable t) {
                Logger.getCurrentLogger().warn("Event handler of " + registeredEvent.getPlugin() + " threw exception", t);
            }
        }
        return value;
    }
}
