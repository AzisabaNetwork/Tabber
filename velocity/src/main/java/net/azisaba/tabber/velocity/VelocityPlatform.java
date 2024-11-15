package net.azisaba.tabber.velocity;

import com.velocitypowered.api.scheduler.ScheduledTask;
import net.azisaba.tabber.api.impl.AbstractTabberPlatform;
import org.jetbrains.annotations.NotNull;

public class VelocityPlatform extends AbstractTabberPlatform {
    private final @NotNull VelocityTabber tabber;

    public VelocityPlatform(@NotNull VelocityTabber tabber) {
        this.tabber = tabber;
    }

    @Override
    public void cancelTasks() {
        tabber.getPlugin().getProxyServer().getScheduler().tasksByPlugin(tabber.getPlugin()).forEach(ScheduledTask::cancel);
    }
}
