package net.azisaba.tabber.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.azisaba.tabber.api.Constants;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.velocity.actor.VelocityTabberPlayer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "tabber",
        version = Constants.VERSION,
        name = "Tabber",
        description = "Tab list plugin for Velocity",
        dependencies = {
                @Dependency(id = "tab"),
                @Dependency(id = "velocity-scoreboard-api"),
        },
        url = "https://github.com/AzisabaNetwork/Tabber",
        authors = {"AzisabaNetwork", "acrylic-style"}
)
public class VelocityPlugin {
    private final @NotNull Logger logger;
    private final @NotNull ProxyServer proxyServer;
    private final @NotNull Path dataDirectory;
    private final @NotNull VelocityTabberCommand command = new VelocityTabberCommand();

    @Inject
    public VelocityPlugin(@NotNull Logger logger, @NotNull ProxyServer proxyServer, @DataDirectory @NotNull Path dataDirectory) {
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.dataDirectory = dataDirectory;
        TabberProvider.setInstance(new VelocityTabber(this));
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    public @NotNull ProxyServer getProxyServer() {
        return proxyServer;
    }

    public @NotNull Path getDataDirectory() {
        return dataDirectory;
    }

    public @NotNull VelocityTabberCommand getCommand() {
        return command;
    }

    @Subscribe
    public void onProxyInitialization(@NotNull ProxyInitializeEvent e) {
        TabberProvider.get().enable();
        proxyServer.getCommandManager().register(proxyServer.getCommandManager().metaBuilder("tabber").plugin(this).build(), command);
    }

    @Subscribe
    public void onProxyShutdown(@NotNull ProxyShutdownEvent e) {
        TabberProvider.get().disable();
    }

    @Subscribe
    public void onPostLogin(@NotNull PostLoginEvent e) {
        TabberProvider.get().getPlatform().onJoin(new VelocityTabberPlayer(e.getPlayer()));
    }

    @SuppressWarnings("UnstableApiUsage")
    @Subscribe
    public void onServerPostConnect(@NotNull ServerPostConnectEvent e) {
        TabberProvider.get().getPlatform().onJoin(new VelocityTabberPlayer(e.getPlayer()));
    }
}
