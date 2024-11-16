package net.azisaba.tabber.api.impl.config;

import dev.cel.common.CelAbstractSyntaxTree;
import dev.cel.common.CelValidationException;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.config.TabberConfig;
import net.azisaba.tabber.api.core.Registry;
import net.azisaba.tabber.api.order.OrderData;
import net.azisaba.tabber.api.order.OrderType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TabberConfigImpl implements TabberConfig {
    private final @NotNull ConfigurationNode root;
    private final CelRuntime.@NotNull Program disableOrderExpression;
    private final @NotNull List<@NotNull OrderData> order;

    private TabberConfigImpl(@NotNull ConfigurationNode root) {
        this.root = Objects.requireNonNull(root);
        Registry<String, OrderType> orderTypeRegistry = TabberProvider.get().getOrderTypeRegistry();
        CelRuntime.@NotNull Program disableOrderExpression1;
        try {
            String expression = Objects.requireNonNull(root.node("disable-order-expression").getString(), "disable-order-expression");
            CelAbstractSyntaxTree ast = OrderData.CEL_COMPILER.get().compile(expression).getAst();
            disableOrderExpression1 = OrderData.CEL_RUNTIME.get().createProgram(ast);
        } catch (Exception e) {
            Logger.getCurrentLogger().warn("Failed to compile disable-order-expression: {}", root.node("disable-order-expression"), e);
            try {
                disableOrderExpression1 = OrderData.CEL_RUNTIME.get().createProgram(OrderData.CEL_COMPILER.get().compile("false").getAst());
            } catch (CelEvaluationException | CelValidationException ex) {
                throw new RuntimeException(ex);
            }
        }
        this.disableOrderExpression = disableOrderExpression1;
        this.order = root.node("order")
                .childrenList()
                .stream()
                .map(node -> {
                    try {
                        String typeString = Objects.requireNonNull(node.node("type").getString(), "type").toUpperCase(Locale.ROOT);
                        OrderType type = orderTypeRegistry.getOrThrow(typeString);
                        String expression = Objects.requireNonNull(node.node("expression").getString(), "expression");
                        return OrderData.orderData(type, expression);
                    } catch (Exception e) {
                        Logger.getCurrentLogger().error("Failed to parse order data", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
    public @NotNull List<@NotNull String> getSpyServers() {
        try {
            return Objects.requireNonNull(root.node("spy-servers").getList(String.class), "spy-servers cannot be null; use empty list instead");
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isolateUnlistedServers() {
        return root.node("isolate-unlisted-servers").getBoolean(false);
    }

    @Override
    public CelRuntime.@NotNull Program getDisableOrderExpression() {
        return disableOrderExpression;
    }

    @Override
    public int getOrderUpdateInterval() {
        return root.node("order-update-interval").getInt(5000);
    }

    @Override
    public @NotNull List<@NotNull OrderData> getOrder() {
        return order;
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
