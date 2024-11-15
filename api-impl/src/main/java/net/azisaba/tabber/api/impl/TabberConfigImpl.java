package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabberConfigImpl implements TabberConfig {
    private final @NotNull ConfigurationNode root;

    private TabberConfigImpl(@NotNull ConfigurationNode root) {
        this.root = Objects.requireNonNull(root);
    }

    public @NotNull ConfigurationNode getRoot() {
        return root;
    }

    @Override
    public boolean isDebug() {
        return root.node("debug").getBoolean(false);
    }

    @Override
    public @NotNull Map<@NotNull String, @NotNull List<@NotNull String>> getServerGroups() {
        return root.node("server-groups").childrenMap().values().stream()
                .collect(HashMap::new, (map, node) -> {
                    String key = Objects.requireNonNull(node.key(), "key of " + node).toString();
                    List<String> value = node.childrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
                    map.put(key, value);
                }, HashMap::putAll);
    }

    @Override
    public boolean isolateUnlistedServers() {
        return root.node("isolate-unlisted-servers").getBoolean(false);
    }

    @Override
    public int getOrderUpdateInterval() {
        return root.node("order-update-interval").getInt(5000);
    }

    @Override
    public boolean isOrderViewerServerFirst() {
        return root.node("order-viewer-server-first").getBoolean(false);
    }

    @Override
    public @NotNull List<@NotNull String> getOrder() {
        return root.node("order")
                .childrenList()
                .stream()
                .map(node -> Objects.requireNonNull(node.getString(), "order contains null"))
                .collect(Collectors.toList());
    }

    /* static methods */

    private static void saveDefaultConfig(@NotNull Path path) throws IOException {
        try (InputStream in = TabberConfigImpl.class.getClassLoader().getResourceAsStream("config.yml")) {
            if (in == null) {
                throw new IOException("Resource not found: config.yml");
            }
            Files.write(path, in.readAllBytes());
        }
    }

    public static @NotNull TabberConfigImpl createFromYaml(@NotNull Path path) {
        if (Files.notExists(path)) {
            try {
                saveDefaultConfig(path);
            } catch (IOException e) {
                Logger.getCurrentLogger().error("Failed to save default config", e);
            }
        }
        try {
            return createFromNode(YamlConfigurationLoader.builder().file(path.toFile()).build().load());
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull TabberConfigImpl createFromNode(@NotNull ConfigurationNode node) {
        return new TabberConfigImpl(node);
    }
}
