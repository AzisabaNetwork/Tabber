package net.azisaba.tabber.api.actor;

import net.azisaba.tabber.api.scoreboard.Scoreboard;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TabberPlayer extends Audience {
    /**
     * Returns the unique ID of this player.
     * @return the unique ID
     */
    @NotNull UUID getUniqueId();

    /**
     * Returns the name of this player.
     * @return the name
     */
    @NotNull String getUsername();

    /**
     * Returns the scoreboard of this player.
     * @return the scoreboard
     */
    @NotNull Scoreboard getScoreboard();
}
