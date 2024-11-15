package net.azisaba.tabber.api.actor;

import net.azisaba.tabber.api.scoreboard.Scoreboard;
import net.azisaba.tabber.message.Player;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
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
     * Returns the server name that this player is currently connected to.
     * @return the server name
     */
    @NotNull Optional<@NotNull String> getServerName();

    /**
     * Returns the scoreboard of this player.
     * @return the scoreboard
     */
    @NotNull Scoreboard getScoreboard();

    /**
     * Updates the order of the tab list seen by this player.
     */
    default void updateOrder() {
        Scoreboard scoreboard = getScoreboard();
        // TODO: implement
    }

    /**
     * Converts the player to a protobuf message object.
     * <p>Note: After converting, the message cannot be converted back to the player object.</p>
     * @return the message object
     */
    default @NotNull Player toProtobuf() {
        Player.Builder builder = Player.newBuilder()
                .setUsername(getUsername())
                .setUuid(getUniqueId().toString());
        getServerName().ifPresent(builder::setServer);
        return builder.build();
    }
}
