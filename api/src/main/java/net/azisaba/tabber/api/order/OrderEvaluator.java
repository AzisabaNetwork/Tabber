package net.azisaba.tabber.api.order;

import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import net.azisaba.tabber.api.actor.TabberPlayer;
import org.jetbrains.annotations.NotNull;

public interface OrderEvaluator {
    @NotNull String evaluate(@NotNull TabberPlayer viewer, @NotNull TabberPlayer player, @NotNull CelRuntime.Program expression) throws CelEvaluationException;
}
