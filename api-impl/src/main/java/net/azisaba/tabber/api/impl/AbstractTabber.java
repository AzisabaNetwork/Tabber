package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.Tabber;
import net.azisaba.tabber.api.impl.function.FunctionManagerImpl;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTabber implements Tabber {
    protected final FunctionManagerImpl functionManager = new FunctionManagerImpl();

    @Override
    public @NotNull FunctionManagerImpl getFunctionManager() {
        return functionManager;
    }
}
