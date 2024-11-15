package net.azisaba.tabber.api.macro;

import dev.cel.parser.CelMacro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;

public interface MacroManager {
    /**
     * Registers a new macro.
     * @param macro the macro to register
     */
    void registerMacro(@NotNull CelMacro macro);

    /**
     * Unregisters a macro.
     * @param macro the macro to unregister
     */
    void unregisterMacro(@NotNull CelMacro macro);

    /**
     * Returns all registered macros.
     * @return the registered macros
     */
    @UnmodifiableView
    @NotNull Collection<@NotNull CelMacro> getRegisteredMacros();

    /**
     * Registers default macros.
     */
    default void load() {
        for (TabberMacros macro : TabberMacros.values()) {
            registerMacro(macro.getMacro());
        }
    }

    /**
     * Unregisters all macros.
     */
    void unload();
}
