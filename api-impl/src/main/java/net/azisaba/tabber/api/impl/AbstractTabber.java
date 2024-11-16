package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.Tabber;
import net.azisaba.tabber.api.core.MutableRegistry;
import net.azisaba.tabber.api.impl.core.MutableRegistryImpl;
import net.azisaba.tabber.api.impl.function.FunctionManagerImpl;
import net.azisaba.tabber.api.order.OrderType;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTabber implements Tabber {
    protected final FunctionManagerImpl functionManager = new FunctionManagerImpl();
    protected final MutableRegistry<String, OrderType> orderTypeRegistry = new MutableRegistryImpl<>();

    @Override
    public @NotNull FunctionManagerImpl getFunctionManager() {
        return functionManager;
    }

    @Override
    public @NotNull MutableRegistry<String, OrderType> getOrderTypeRegistry() {
        return orderTypeRegistry;
    }
}
