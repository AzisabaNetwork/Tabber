package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.Tabber;
import net.azisaba.tabber.api.impl.macro.MacroManagerImpl;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTabber implements Tabber {
    protected final MacroManagerImpl macroManager = new MacroManagerImpl();

    @Override
    public @NotNull MacroManagerImpl getMacroManager() {
        return macroManager;
    }
}
