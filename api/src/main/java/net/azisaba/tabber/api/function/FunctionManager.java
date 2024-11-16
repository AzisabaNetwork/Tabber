package net.azisaba.tabber.api.function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface FunctionManager {
    /**
     * Registers a new function.
     * @param function the function to register
     */
    void registerFunction(@NotNull CelFunction function);

    /**
     * Unregisters a function.
     * @param function the function to unregister
     */
    void unregisterFunction(@NotNull CelFunction function);

    /**
     * Returns all registered functions.
     * @return the registered functions
     */
    @UnmodifiableView
    @NotNull Collection<@NotNull CelFunction> getRegisteredFunctions();

    /**
     * Registers default functions.
     */
    default void load() {
        registerFunction(CelFunction.STRING_REVERSE);
        registerFunction(CelFunction.PLAYER_GET_PLACEHOLDER);
        registerFunction(CelFunction.PLAYER_GET_PLACEHOLDER_AS_INT);
        registerFunction(CelFunction.LIST_INDEX_OF);
    }

    /**
     * Unregisters all functions.
     */
    void unload();
}
