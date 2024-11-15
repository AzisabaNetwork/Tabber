package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.TabberPlatform;
import net.azisaba.tabber.api.actor.TabberPlayer;
import org.jetbrains.annotations.NotNull;

public class AbstractTabberPlatform implements TabberPlatform {
    @Override
    public void onJoin(@NotNull TabberPlayer player) {
        // reorder the player list
    }
}
