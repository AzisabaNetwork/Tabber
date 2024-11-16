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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record VelocityTabberPlayer(@NotNull Player player) implements TabberPlayer, ForwardingAudience.Single {
    private static final Set<UUID> SPY_PLAYERS = ConcurrentHashMap.newKeySet();

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

    @Override
    public boolean isSpy() {
        return SPY_PLAYERS.contains(getUniqueId());
    }

    @Override
    public void setSpy(boolean spy) {
        if (spy) {
            SPY_PLAYERS.add(getUniqueId());
        } else {
            SPY_PLAYERS.remove(getUniqueId());
        }
    }

    public @NotNull TabPlayer getTabPlayer() {
        return Objects.requireNonNull(TAB.getInstance().getPlayer(getUniqueId()), "TabPlayer is null");
    }
}
