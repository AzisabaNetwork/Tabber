package net.azisaba.tabber.api.order;

import dev.cel.common.CelAbstractSyntaxTree;
import dev.cel.common.CelValidationException;
import dev.cel.common.types.CelTypes;
import dev.cel.compiler.CelCompiler;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.extensions.CelExtensions;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelEvaluationException;
import dev.cel.runtime.CelRuntime;
import dev.cel.runtime.CelRuntimeFactory;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.function.CelFunction;
import net.azisaba.tabber.api.util.LazyValue;
import net.azisaba.tabber.message.PlayerMessage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collectors;

public final class OrderData {
    @ApiStatus.Internal
    public static final @NotNull LazyValue<CelCompiler> CEL_COMPILER =
            new LazyValue<>(() ->
                    CelCompilerFactory.standardCelCompilerBuilder()
                            .setStandardMacros(CelStandardMacro.values())
                            .addLibraries(CelExtensions.strings())
                            .addLibraries(CelExtensions.lists())
                            .addMessageTypes(PlayerMessage.getDescriptor().getMessageTypes())
                            .addFunctionDeclarations(
                                    TabberProvider.get()
                                            .getFunctionManager()
                                            .getRegisteredFunctions()
                                            .stream()
                                            .map(CelFunction::getFunction)
                                            .collect(Collectors.toList()))
                            .addVar("viewer", CelTypes.createMessage("Player"))
                            .addVar("player", CelTypes.createMessage("Player"))
                            .build());
    @ApiStatus.Internal
    public static final @NotNull LazyValue<CelRuntime> CEL_RUNTIME =
            new LazyValue<>(() ->
                    CelRuntimeFactory.standardCelRuntimeBuilder()
                            .addLibraries(CelExtensions.strings())
                            .addLibraries(CelExtensions.lists())
                            .addFunctionBindings(
                                    TabberProvider.get()
                                            .getFunctionManager()
                                            .getRegisteredFunctions()
                                            .stream()
                                            .flatMap(f -> f.getBinding().stream())
                                            .collect(Collectors.toList()))
                            .build());
    private final @NotNull OrderType type;
    private final @NotNull CelRuntime.Program expression;

    private OrderData(@NotNull OrderType type, @NotNull CelRuntime.Program expression) {
        this.type = Objects.requireNonNull(type);
        this.expression = Objects.requireNonNull(expression);
    }

    @Contract("_, _ -> new")
    public static @NotNull OrderData orderData(@NotNull OrderType type, @NotNull CelRuntime.Program expression) {
        return new OrderData(type, expression);
    }

    public static @NotNull OrderData orderData(@NotNull OrderType type, @NotNull String expression) {
        try {
            CelAbstractSyntaxTree syntaxTree = CEL_COMPILER.get().compile(expression).getAst();
            CelRuntime.Program program = CEL_RUNTIME.get().createProgram(syntaxTree);
            return orderData(type, program);
        } catch (CelValidationException | CelEvaluationException e) {
            throw new IllegalArgumentException("Failed to compile the expression: " + expression, e);
        }
    }

    public @NotNull OrderType getType() {
        return type;
    }

    public @NotNull CelRuntime.Program getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "type=" + type +
                ", expression='" + expression + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderData orderData = (OrderData) o;
        return Objects.equals(type, orderData.type) && Objects.equals(expression, orderData.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, expression);
    }
}
