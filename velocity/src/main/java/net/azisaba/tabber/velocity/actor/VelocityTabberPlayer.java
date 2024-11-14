package net.azisaba.tabber.velocity.actor;

import com.velocitypowered.api.proxy.Player;
import me.neznamy.tab.shared.TAB;
import me.neznamy.tab.shared.platform.TabPlayer;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class VelocityTabberPlayer implements TabberPlayer, ForwardingAudience.Single {
    private final Player player;

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

    public @NotNull TabPlayer getTabPlayer() {
        return Objects.requireNonNull(TAB.getInstance().getPlayer(getUniqueId()), "TabPlayer is null");
    }
}
