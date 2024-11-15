package net.azisaba.tabber.velocity.actor;

import com.velocitypowered.api.proxy.Player;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.platform.TabPlayer;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.scoreboard.Scoreboard;
import net.azisaba.tabber.velocity.scoreboard.VelocityScoreboard;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record VelocityTabberPlayer(@NotNull Player player) implements TabberPlayer, ForwardingAudience.Single {
    public VelocityTabberPlayer(@NotNull Player player) {
        this.player = Objects.requireNonNull(player);
    }

    @Override
    public @NotNull Player audience() {
        return player;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getUsername() {
        return player.getUsername();
    }

    @Override
    public @NotNull Optional<@NotNull String> getServerName() {
        return player.getCurrentServer().map(conn -> conn.getServerInfo().getName());
    }

    @Override
    public @NotNull Scoreboard getScoreboard() {
        return new VelocityScoreboard(this);
    }

    public @NotNull TabPlayer getTabPlayer() {
        return Objects.requireNonNull(TAB.getInstance().getPlayer(getUniqueId()), "TabPlayer is null");
    }
}
