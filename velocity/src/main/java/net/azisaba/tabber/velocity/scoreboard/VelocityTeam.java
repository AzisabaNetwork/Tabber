package net.azisaba.tabber.velocity.scoreboard;

import com.velocitypowered.api.TextHolder;
import com.velocitypowered.api.scoreboard.ProxyTeam;
import net.azisaba.tabber.api.scoreboard.CollisionRule;
import net.azisaba.tabber.api.scoreboard.Team;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public record VelocityTeam(@NotNull ProxyTeam team) implements Team {
    public VelocityTeam(@NotNull ProxyTeam team) {
        this.team = Objects.requireNonNull(team);
    }

    @Override
    public @NotNull String getName() {
        return team.getName();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return team.getDisplayName().getModernText();
    }

    @Override
    public void addEntry(@NotNull String entry) {
        team.addEntry(entry);
    }

    @Override
    public void removeEntry(@NotNull String entry) {
        team.removeEntry(entry);
    }

    @Override
    public void setPrefix(@NotNull Component prefix) {
        team.setPrefix(TextHolder.of(prefix));
    }

    @Override
    public @NotNull Component getPrefix() {
        return team.getPrefix().getModernText();
    }

    @Override
    public void setSuffix(@NotNull Component suffix) {
        team.setSuffix(TextHolder.of(suffix));
    }

    @Override
    public @NotNull Component getSuffix() {
        return team.getSuffix().getModernText();
    }

    @Override
    public @NotNull Collection<String> getEntries() {
        return team.getEntries();
    }

    public record BuilderImpl(@NotNull ProxyTeam.Builder builder) implements Builder {
        public BuilderImpl(@NotNull ProxyTeam.Builder builder) {
            this.builder = Objects.requireNonNull(builder);
        }

        @Override
        public @NotNull Builder displayName(@NotNull Component displayName) {
            builder.displayName(TextHolder.of(displayName));
            return this;
        }

        @Override
        public @NotNull Builder prefix(@NotNull Component prefix) {
            builder.prefix(TextHolder.of(prefix));
            return this;
        }

        @Override
        public @NotNull Builder suffix(@NotNull Component suffix) {
            builder.suffix(TextHolder.of(suffix));
            return this;
        }

        @Override
        public @NotNull Builder collisionRule(@NotNull CollisionRule rule) {
            builder.collisionRule(com.velocitypowered.api.scoreboard.CollisionRule.getByName(rule.getName()));
            return this;
        }

        @Override
        public @NotNull Builder collisionRule(@NotNull String ruleName) {
            builder.collisionRule(com.velocitypowered.api.scoreboard.CollisionRule.getByName(ruleName));
            return this;
        }
    }
}
