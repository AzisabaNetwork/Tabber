package net.azisaba.tabber.api.impl;

import net.azisaba.tabber.api.TabberConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

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

    /* static methods */

    public static @NotNull TabberConfigImpl createFromYaml(@NotNull Path path) {
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
