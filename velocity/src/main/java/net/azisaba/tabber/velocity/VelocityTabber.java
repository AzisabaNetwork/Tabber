package net.azisaba.tabber.velocity;

import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.Tabber;
import net.azisaba.tabber.api.TabberConfig;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.impl.TabberConfigImpl;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import net.azisaba.tabber.velocity.actor.VelocityTabberPlayer;
import net.azisaba.tabber.velocity.command.VelocityCommandManager;
import net.azisaba.tabber.velocity.placeholder.PlaceholderManagerImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VelocityTabber implements Tabber {
    private final @NotNull VelocityPlugin plugin;
    private final @NotNull Logger logger;
    private final @NotNull VelocityCommandManager commandManager = new VelocityCommandManager(this);
    private final @NotNull PlaceholderManager placeholderManager = new PlaceholderManagerImpl();
    private @NotNull TabberConfig config;

    public VelocityTabber(@NotNull VelocityPlugin plugin) {
        this.plugin = plugin;
        this.logger = Logger.createByProxy(plugin.getLogger());
        this.config = TabberConfigImpl.createFromYaml(plugin.getDataDirectory().resolve("config.yml"));
    }

    public @NotNull VelocityPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void reloadConfig() {
        config = TabberConfigImpl.createFromYaml(plugin.getDataDirectory().resolve("config.yml"));
    }

    @Override
    public @NotNull TabberConfig getConfig() {
        return config;
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
