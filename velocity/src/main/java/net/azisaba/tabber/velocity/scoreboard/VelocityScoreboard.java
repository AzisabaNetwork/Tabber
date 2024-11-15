package net.azisaba.tabber.velocity.scoreboard;

import com.velocitypowered.api.scoreboard.ProxyScoreboard;
import com.velocitypowered.api.scoreboard.ScoreboardManager;
import net.azisaba.tabber.api.scoreboard.Scoreboard;
import net.azisaba.tabber.api.scoreboard.Team;
import net.azisaba.tabber.velocity.actor.VelocityTabberPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;
import java.util.Optional;

public class VelocityScoreboard implements Scoreboard {
    private final @NotNull ProxyScoreboard scoreboard;

    public VelocityScoreboard(VelocityTabberPlayer player) {
        this.scoreboard = ScoreboardManager.getInstance().getProxyScoreboard(player.player());
    }

    public @NotNull ProxyScoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public @NotNull Optional<@NotNull Team> getTeam(@NotNull String name) {
        return Optional.ofNullable(scoreboard.getTeam(name)).map(VelocityTeam::new);
    }

    @Override
    public @UnmodifiableView @NotNull List<@NotNull Team> getTeams() {
        return scoreboard.getTeams().stream().map(team -> (Team) new VelocityTeam(team)).toList();
    }

    @Override
    public void registerTeam(Team.@NotNull Builder builder) {
        VelocityTeam.BuilderImpl builderImpl = (VelocityTeam.BuilderImpl) builder;
        scoreboard.registerTeam(builderImpl.builder());
    }

    @Override
    public void registerTeam(@NotNull String name, @NotNull java.util.function.Consumer<Team.Builder> action) {
        Team.Builder builder = new VelocityTeam.BuilderImpl(scoreboard.teamBuilder(name));
        action.accept(builder);
        registerTeam(builder);
    }

    @Override
    public Team.@NotNull Builder teamBuilder(@NotNull String name) {
        return new VelocityTeam.BuilderImpl(scoreboard.teamBuilder(name));
    }

    @Override
    public void unregisterTeam(@NotNull String name) {
        scoreboard.unregisterTeam(name);
    }
}
