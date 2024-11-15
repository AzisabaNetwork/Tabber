package net.azisaba.tabber.api.impl.function;

import net.azisaba.tabber.api.function.CelFunction;
import net.azisaba.tabber.api.function.FunctionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionManagerImpl implements FunctionManager {
    private final @NotNull Set<@NotNull CelFunction> registeredFunctions = ConcurrentHashMap.newKeySet();

    @Override
    public void registerFunction(@NotNull CelFunction function) {
        registeredFunctions.add(Objects.requireNonNull(function));
    }

    @Override
    public void unregisterFunction(@NotNull CelFunction function) {
        registeredFunctions.remove(Objects.requireNonNull(function));
    }

    @Override
    public @UnmodifiableView @NotNull Collection<@NotNull CelFunction> getRegisteredFunctions() {
        return Collections.unmodifiableSet(registeredFunctions);
    }

    @Override
    public void unload() {
        registeredFunctions.clear();
    }
}
