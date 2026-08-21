package net.azisaba.tabber.velocity;

import com.velocitypowered.api.scheduler.ScheduledTask;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.impl.AbstractTabberPlatform;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VelocityPlatform extends AbstractTabberPlatform {
    private final @NotNull VelocityTabber tabber;

    public VelocityPlatform(@NotNull VelocityTabber tabber) {
        this.tabber = tabber;
    }

    @Override
    public void cancelTasks() {
        tabber.getPlugin().getProxyServer().getScheduler().tasksByPlugin(tabber.getPlugin()).forEach(ScheduledTask::cancel);
    }

    @Override
    public void scheduleOrderUpdateTask() {
        tabber.getPlugin().getProxyServer().getScheduler().buildTask(tabber.getPlugin(), () -> {
            if (!tabber.isEnabled()) {
                return;
            }
            for (@NotNull TabberPlayer viewer : tabber.getOnlinePlayers()) {
                try {
                    viewer.updateOrder();
                } catch (RuntimeException e) {
                    tabber.getLogger().error("Failed to update tab order for player " + viewer.getUsername(), e);
                }
            }
        }).repeat(tabber.getConfig().getOrderUpdateInterval(), TimeUnit.MILLISECONDS).schedule();
    }
}
