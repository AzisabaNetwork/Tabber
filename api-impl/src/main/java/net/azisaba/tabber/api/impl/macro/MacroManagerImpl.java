package net.azisaba.tabber.api.impl.macro;

import dev.cel.parser.CelMacro;
import net.azisaba.tabber.api.macro.MacroManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MacroManagerImpl implements MacroManager {
    private final @NotNull Set<@NotNull CelMacro> registeredMacros = ConcurrentHashMap.newKeySet();

    @Override
    public void registerMacro(@NotNull CelMacro macro) {
        Objects.requireNonNull(macro);
        registeredMacros.add(macro);
    }

    @Override
    public void unregisterMacro(@NotNull CelMacro macro) {
        Objects.requireNonNull(macro);
        registeredMacros.remove(macro);
    }

    @Override
    public @UnmodifiableView @NotNull Collection<@NotNull CelMacro> getRegisteredMacros() {
        return Collections.unmodifiableSet(registeredMacros);
    }

    @Override
    public void unload() {
        registeredMacros.clear();
    }
}
