package net.azisaba.tabber.velocity;

import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.config.TabberConfig;
import net.azisaba.tabber.api.TabberPlatform;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.impl.AbstractTabber;
import net.azisaba.tabber.api.impl.config.TabberConfigImpl;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import net.azisaba.tabber.velocity.actor.VelocityTabberPlayer;
import net.azisaba.tabber.velocity.command.VelocityCommandManager;
import net.azisaba.tabber.velocity.placeholder.PlaceholderManagerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class VelocityTabber extends AbstractTabber {
    private final @NotNull VelocityPlugin plugin;
    private final @NotNull Logger logger;
    private final @NotNull VelocityPlatform platform = new VelocityPlatform(this);
    private final @NotNull VelocityCommandManager commandManager = new VelocityCommandManager(this);
    private final @NotNull PlaceholderManager placeholderManager = new PlaceholderManagerImpl();
    private @Nullable TabberConfig config;

    public VelocityTabber(@NotNull VelocityPlugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.createByProxy(plugin.getLogger());
    }

    public @NotNull VelocityPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull TabberPlatform getPlatform() {
        return platform;
    }

    @Override
    public void reloadConfig() {
        config = TabberConfigImpl.createFromYaml(plugin.getDataDirectory().resolve("config.yml"));
    }

    @Override
    public @NotNull TabberConfig getConfig() {
        return Objects.requireNonNull(config, "config is null");
    }

    @Override
    public @NotNull Logger getLogger() {
        return logger;
    }

    @Override
    public @NotNull VelocityCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public @NotNull PlaceholderManager getPlaceholderManager() {
        return placeholderManager;
    }

    @Override
    public @NotNull Optional<@NotNull TabberPlayer> getPlayer(@NotNull String username) {
        return plugin.getProxyServer().getPlayer(username).map(VelocityTabberPlayer::new);
    }

    @Override
    public @NotNull Optional<@NotNull TabberPlayer> getPlayer(@NotNull UUID uuid) {
        return plugin.getProxyServer().getPlayer(uuid).map(VelocityTabberPlayer::new);
    }

    @Override
    public @NotNull List<@NotNull TabberPlayer> getOnlinePlayers() {
        return plugin.getProxyServer().getAllPlayers().stream().map(player -> (TabberPlayer) new VelocityTabberPlayer(player)).toList();
    }
}
