package net.azisaba.tabber.api.function;

import dev.cel.common.CelFunctionDecl;
import dev.cel.common.CelOverloadDecl;
import dev.cel.common.types.CelType;
import dev.cel.runtime.CelRuntime;
import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.message.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CelFunction {
    public static final CelFunction STRING_REVERSE = new CelFunction(
            "reverse",
            OverloadDef.createMemberOverload(
                    "String_reverse",
                    String.class,
                    String.class,
                    (string) -> new StringBuilder(string).reverse().toString()
            )
    );
    public static final CelFunction PLAYER_GET_PLACEHOLDER = new CelFunction(
            "getPlaceholder",
            OverloadDef.createMemberOverload(
                    "Player_getPlaceholder_string",
                    String.class,
                    Player.class,
                    String.class,
                    (player, identifier) -> {
                        TabberPlayer tabberPlayer = TabberProvider.get().getPlayer(UUID.fromString(player.getUuid())).orElseThrow();
                        return TabberProvider.get().getPlaceholderManager().getPlaceholderByIdentifier(identifier)
                                .map(placeholder -> placeholder.replace(identifier, tabberPlayer))
                                .orElse(identifier);
                    }
            )
    );
    public static final CelFunction PLAYER_GET_PLACEHOLDER_AS_INT = new CelFunction(
            "getPlaceholderAsInt",
            OverloadDef.createMemberOverload(
                    "Player_getPlaceholderAsInt_string",
                    int.class,
                    Player.class,
                    String.class,
                    (player, identifier) -> {
                        TabberPlayer tabberPlayer = TabberProvider.get().getPlayer(UUID.fromString(player.getUuid())).orElseThrow();
                        return TabberProvider.get().getPlaceholderManager().getPlaceholderByIdentifier(identifier)
                                .map(placeholder -> placeholder.replace(identifier, tabberPlayer))
                                .map(value -> {
                                    try {
                                        return Integer.parseInt(value);
                                    } catch (NumberFormatException e) {
                                        if (TabberProvider.get().getConfig().isDebug()) {
                                            Logger.getCurrentLogger().warn("Failed to parse int: " + value, e);
                                        } else {
                                            Logger.getCurrentLogger().warn("Failed to parse int: " + value);
                                        }
                                        return 0;
                                    }
                                })
                                .orElse(0);
                    }
            )
    );

    private final @NotNull CelFunctionDecl function;
    private final @NotNull List<CelRuntime.@NotNull CelFunctionBinding> binding;

    public CelFunction(@NotNull CelFunctionDecl function, @NotNull List<CelRuntime.@NotNull CelFunctionBinding> binding) {
        this.function = Objects.requireNonNull(function);
        this.binding = Objects.requireNonNull(binding);
    }

    public CelFunction(@NotNull CelFunctionDecl function, CelRuntime.@NotNull CelFunctionBinding @NotNull ... binding) {
        this(function, List.of(binding));
    }

    public CelFunction(@NotNull String name, @NotNull List<@NotNull OverloadDef> overloadDefs) {
        this.function = CelFunctionDecl.newBuilder()
                .setName(name)
                .addOverloads(overloadDefs.stream()
                        .map(def -> CelOverloadDecl.newMemberOverload(
                                def.overloadId,
                                CelTypeResolver.resolve(def.returnType),
                                def.args.stream()
                                        .map(CelTypeResolver::resolve)
                                        .toArray(CelType[]::new)
                        ))
                        .toArray(CelOverloadDecl[]::new)
                ).build();
        this.binding = overloadDefs.stream()
                .map(def -> CelRuntime.CelFunctionBinding.from(def.overloadId, def.args, (args) -> def.action.apply(List.of(args))))
                .collect(Collectors.toList());
    }

    public CelFunction(@NotNull String name, @NotNull OverloadDef @NotNull ... overloadDefs) {
        this(name, List.of(overloadDefs));
    }

    public @NotNull CelFunctionDecl getFunction() {
        return function;
    }

    public @NotNull List<CelRuntime.@NotNull CelFunctionBinding> getBinding() {
        return binding;
    }

    public static class OverloadDef {
        public final @NotNull String overloadId;
        public final @NotNull Class<?> returnType;
        public final @NotNull List<@NotNull Class<?>> args;
        public final @NotNull Function<List<Object>, Object> action;

        public OverloadDef(@NotNull String overloadId, @NotNull Class<?> returnType, @NotNull List<@NotNull Class<?>> args, @NotNull Function<@NotNull List<Object>, Object> action) {
            this.overloadId = overloadId;
            this.returnType = returnType;
            this.args = args;
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        public static <T0, R> @NotNull OverloadDef createMemberOverload(
                @NotNull String overloadId,
                @NotNull Class<R> returnType,
                @NotNull Class<T0> owner,
                Function<T0, R> action
        ) {
            return new OverloadDef(overloadId, returnType, List.of(owner), (args) -> {
                T0 ownerValue = (T0) args.get(0);
                return action.apply(ownerValue);
            });
        }

        @SuppressWarnings("unchecked")
        public static <T0, T1, R> @NotNull OverloadDef createMemberOverload(
                @NotNull String overloadId,
                @NotNull Class<R> returnType,
                @NotNull Class<T0> owner,
                @NotNull Class<T1> arg1,
                BiFunction<T0, T1, R> action
        ) {
            return new OverloadDef(overloadId, returnType, List.of(owner, arg1), (args) -> {
                T0 ownerValue = (T0) args.get(0);
                T1 arg1Value = (T1) args.get(1);
                return action.apply(ownerValue, arg1Value);
            });
        }

        @SuppressWarnings("unchecked")
        public static <T0, T1, T2, R> @NotNull OverloadDef createMemberOverload(
                @NotNull String overloadId,
                @NotNull Class<R> returnType,
                @NotNull Class<T0> owner,
                @NotNull Class<T1> arg1,
                @NotNull Class<T2> arg2,
                Function3<T0, T1, T2, R> action
        ) {
            return new OverloadDef(overloadId, returnType, List.of(owner, arg1, arg2), (args) -> {
                T0 ownerValue = (T0) args.get(0);
                T1 arg1Value = (T1) args.get(1);
                T2 arg2Value = (T2) args.get(2);
                return action.apply(ownerValue, arg1Value, arg2Value);
            });
        }

        @SuppressWarnings("unchecked")
        public static <T0, T1, T2, T3, R> @NotNull OverloadDef createMemberOverload(
                @NotNull String overloadId,
                @NotNull Class<R> returnType,
                @NotNull Class<T0> owner,
                @NotNull Class<T1> arg1,
                @NotNull Class<T2> arg2,
                @NotNull Class<T3> arg3,
                Function4<T0, T1, T2, T3, R> action
        ) {
            return new OverloadDef(overloadId, returnType, List.of(owner, arg1, arg2, arg3), (args) -> {
                T0 ownerValue = (T0) args.get(0);
                T1 arg1Value = (T1) args.get(1);
                T2 arg2Value = (T2) args.get(2);
                T3 arg3Value = (T3) args.get(3);
                return action.apply(ownerValue, arg1Value, arg2Value, arg3Value);
            });
        }

        @SuppressWarnings("unchecked")
        public static <T0, T1, T2, T3, T4, R> @NotNull OverloadDef createMemberOverload(
                @NotNull String overloadId,
                @NotNull Class<R> returnType,
                @NotNull Class<T0> owner,
                @NotNull Class<T1> arg1,
                @NotNull Class<T2> arg2,
                @NotNull Class<T3> arg3,
                @NotNull Class<T4> arg4,
                Function5<T0, T1, T2, T3, T4, R> action
        ) {
            return new OverloadDef(overloadId, returnType, List.of(owner, arg1, arg2, arg3, arg4), (args) -> {
                T0 ownerValue = (T0) args.get(0);
                T1 arg1Value = (T1) args.get(1);
                T2 arg2Value = (T2) args.get(2);
                T3 arg3Value = (T3) args.get(3);
                T4 arg4Value = (T4) args.get(4);
                return action.apply(ownerValue, arg1Value, arg2Value, arg3Value, arg4Value);
            });
        }
    }
}
