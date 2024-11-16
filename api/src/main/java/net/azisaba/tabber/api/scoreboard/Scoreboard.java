package net.azisaba.tabber.api.scoreboard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Scoreboard {
    /**
     * Get a team by its name.
     * @param name the name of the team
     * @return the team, if found
     */
    @NotNull Optional<@NotNull Team> getTeam(@NotNull String name);

    /**
     * Get all teams in this scoreboard.
     * @return the teams
     */
    @UnmodifiableView
    @NotNull List<@NotNull Team> getTeams();

    /**
     * Register a team to this scoreboard.
     * @param builder the builder of the team
     */
    @NotNull Team registerTeam(@NotNull Team.Builder builder);

    /**
     * Register a team to this scoreboard.
     * @param name the name of the team
     */
    default @NotNull Team registerTeam(@NotNull String name) {
        return registerTeam(name, team -> {});
    }

    /**
     * Register a team to this scoreboard.
     * @param name the name of the team
     * @param action the action to configure the team
     */
    @NotNull Team registerTeam(@NotNull String name, @NotNull Consumer<Team.Builder> action);

    /**
     * Create a new team builder.
     * @param name the name of the team
     */
    @NotNull Team.Builder teamBuilder(@NotNull String name);

    /**
     * Unregister a team from this scoreboard.
     * @param name the name of the team
     * @throws IllegalStateException if the team is not found
     */
    void unregisterTeam(@NotNull String name) throws IllegalStateException;

    default @NotNull Optional<@NotNull Team> findTeam(@NotNull String entry) {
        return getTeams().stream().filter(team -> team.getEntries().contains(entry)).findAny();
    }
}
