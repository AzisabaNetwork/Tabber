package net.azisaba.tabber.api.order;

import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.util.NumberUtil;
import net.azisaba.tabber.api.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a (sort) order type.
 */
public final class OrderType {
    // pre-defined order types
    public static final @NotNull OrderType ASCENDING = new OrderType(
            "ASCENDING",
            (viewer, player, expression) ->
                    String.valueOf(expression.eval(Map.of("viewer", viewer.toProtobuf(), "player", player.toProtobuf())))
    );
    public static final @NotNull OrderType DESCENDING = new OrderType(
            "DESCENDING",
            (viewer, player, expression) ->
                    StringUtil.convertZtoA(String.valueOf(expression.eval(Map.of("viewer", viewer.toProtobuf(), "player", player.toProtobuf()))))
    );
    public static final @NotNull OrderType LOW_TO_HIGH = new OrderType(
            "HIGH_TO_LOW",
            (viewer, player, expression) -> {
                String value = String.valueOf(expression.eval(Map.of("viewer", viewer.toProtobuf(), "player", player.toProtobuf())));
                double number;
                try {
                    number = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    if (TabberProvider.get().getConfig().isDebug()) {
                        Logger.getCurrentLogger().warn("Failed to parse number (LOW_TO_HIGH): " + value, e);
                    } else {
                        Logger.getCurrentLogger().warn("Failed to parse number (LOW_TO_HIGH): " + value);
                    }
                    number = 0.0;
                }
                return NumberUtil.compressNumber(number);
            }
    );
    public static final @NotNull OrderType HIGH_TO_LOW = new OrderType(
            "HIGH_TO_LOW",
            (viewer, player, expression) -> {
                String value = String.valueOf(expression.eval(Map.of("viewer", viewer.toProtobuf(), "player", player.toProtobuf())));
                double number;
                try {
                    number = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    if (TabberProvider.get().getConfig().isDebug()) {
                        Logger.getCurrentLogger().warn("Failed to parse number (HIGH_TO_LOW): " + value, e);
                    } else {
                        Logger.getCurrentLogger().warn("Failed to parse number (HIGH_TO_LOW): " + value);
                    }
                    number = 0.0;
                }
                return NumberUtil.compressNumberReverse(number);
            }
    );

    private final @NotNull String name;
    private final @NotNull OrderEvaluator evaluator;

    /**
     * Constructs a new OrderType with the given name. The name should be unique, and uppercase.
     * @param name the name of the order type
     */
    public OrderType(@NotNull String name, @NotNull OrderEvaluator evaluator) {
        if (!Objects.requireNonNull(name, "name").toUpperCase(Locale.ROOT).equals(name)) {
            throw new IllegalArgumentException("OrderType name should be uppercase");
        }
        this.name = name;
        this.evaluator = Objects.requireNonNull(evaluator, "evaluator");
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull OrderEvaluator getEvaluator() {
        return evaluator;
    }
}
