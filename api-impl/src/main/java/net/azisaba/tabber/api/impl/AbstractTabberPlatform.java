package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.TabberPlatform;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTabberPlatform implements TabberPlatform {
    @Override
    public void onJoin(@NotNull TabberPlayer player) {
        for (@NotNull TabberPlayer viewer : TabberProvider.get().getOnlinePlayers()) {
            // reorder the player list
            viewer.updateOrder();
        }
    }
}
