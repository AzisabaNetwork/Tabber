package net.azisaba.tabber.api.actor;

import dev.cel.runtime.CelEvaluationException;
import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.order.OrderData;
import net.azisaba.tabber.api.scoreboard.Scoreboard;
import net.azisaba.tabber.api.scoreboard.Team;
import net.azisaba.tabber.api.util.StringUtil;
import net.azisaba.tabber.message.Player;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
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
     * Returns whether this player is a spy or not. A spy is a player who can see all players on the tab list.
     * @return true if this player is a spy
     */
    boolean isSpy();

    /**
     * Sets whether this player is a spy or not. A spy is a player who can see all players on the tab list.
     * This state persists even after the player logs out. To reset the state, you need to call this method with false.
     * @param spy true if this player is a spy
     */
    void setSpy(boolean spy);

    /**
     * Updates the order of the tab list seen by this player.
     */
    default void updateOrder() {
        try {
            if (Boolean.parseBoolean(String.valueOf(TabberProvider.get().getConfig().getDisableOrderExpression().eval(Map.of("viewer", toProtobuf(), "player", toProtobuf()))))) {
                unregisterScoreboard();
                return;
            }
        } catch (CelEvaluationException e) {
            Logger.getCurrentLogger().warn("Failed to evaluate the expression: " + TabberProvider.get().getConfig().getDisableOrderExpression(), e);
        }
        Scoreboard scoreboard = getScoreboard();
        if (isSpy() || TabberProvider.get().getConfig().getSpyServers().contains(getServerName().orElse(null))) {
            for (@NotNull TabberPlayer player : TabberProvider.get().getOnlinePlayers()) {
                replaceTeam(this, player, scoreboard);
            }
            return;
        }
        String serverGroup = getServerGroup().orElse(null);
        if (serverGroup != null) {
            for (@NotNull TabberPlayer player : TabberProvider.get().getOnlinePlayers()) {
                if (!serverGroup.equals(player.getServerGroup().orElse(null))) {
                    scoreboard.findTeam(player.getUsername()).ifPresent(team -> scoreboard.unregisterTeam(team.getName()));
                    continue;
                }
                replaceTeam(this, player, scoreboard);
            }
            return;
        }
        if (TabberProvider.get().getConfig().isolateUnlistedServers()) {
            for (@NotNull TabberPlayer player : TabberProvider.get().getOnlinePlayers()) {
                if (player.getServerGroup().isPresent()) {
                    scoreboard.findTeam(player.getUsername()).ifPresent(team -> scoreboard.unregisterTeam(team.getName()));
                    continue;
                }
                replaceTeam(this, player, scoreboard);
            }
            return;
        }
        String ourServerName = getServerName().orElse(null);
        for (@NotNull TabberPlayer player : TabberProvider.get().getOnlinePlayers()) {
            if (!Objects.equals(ourServerName, player.getServerName().orElse(null))) {
                scoreboard.findTeam(player.getUsername()).ifPresent(team -> scoreboard.unregisterTeam(team.getName()));
                continue;
            }
            replaceTeam(this, player, scoreboard);
        }
    }

    /**
     * Attempt to find the server group of this player.
     * @return the server group
     */
    default @NotNull Optional<@NotNull String> getServerGroup() {
        return getServerName().map(server -> TabberProvider.get().getConfig().getServerGroups().entrySet().stream()
                .filter(entry -> entry.getValue().contains(server))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(server));
    }

    /**
     * Unregister the scoreboard seen by this player.
     */
    default void unregisterScoreboard() {
        getScoreboard().getTeams().forEach(team -> getScoreboard().unregisterTeam(team.getName()));
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
        getServerGroup().ifPresent(builder::setServerGroup);
        return builder.build();
    }

    /**
     * Calculates the team name for the viewer and player.
     */
    static @NotNull String calculateTeamName(@NotNull TabberPlayer viewer, @NotNull TabberPlayer player) {
        StringBuilder sb = new StringBuilder();
        for (@NotNull OrderData order : TabberProvider.get().getConfig().getOrder()) {
            try {
                sb.append(order.getType().getEvaluator().evaluate(viewer, player, order.getExpression()));
            } catch (Exception e) {
                Logger.getCurrentLogger().warn("Failed to evaluate the expression: " + order.getExpression(), e);
            }
        }
        return sb.toString();
    }

    private static void replaceTeam(@NotNull TabberPlayer viewer, @NotNull TabberPlayer player, @NotNull Scoreboard viewerScoreboard) {
        String teamName = StringUtil.preValidateTeamName(calculateTeamName(viewer, player)); // TODO: add A, B, C at the end
        Team team = viewerScoreboard.findTeam(player.getUsername()).orElse(null);
        if (team == null || !team.getName().equals(teamName)) {
            if (team != null) viewerScoreboard.unregisterTeam(team.getName());
            Team existingTeam = viewerScoreboard.getTeam(teamName).orElse(null);
            Objects.requireNonNullElseGet(existingTeam, () -> viewerScoreboard.registerTeam(teamName))
                    .addEntry(player.getUsername());
        }
    }
}
