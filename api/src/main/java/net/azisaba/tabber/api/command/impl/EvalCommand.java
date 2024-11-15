package net.azisaba.tabber.api.command.impl;

import dev.cel.common.CelAbstractSyntaxTree;
import dev.cel.common.types.CelTypes;
import dev.cel.compiler.CelCompiler;
import dev.cel.compiler.CelCompilerFactory;
import dev.cel.parser.CelStandardMacro;
import dev.cel.runtime.CelRuntime;
import dev.cel.runtime.CelRuntimeFactory;
import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.Command;
import net.azisaba.tabber.api.function.CelFunction;
import net.azisaba.tabber.message.PlayerMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class EvalCommand implements Command {
    private final CelCompiler CEL_COMPILER =
            CelCompilerFactory.standardCelCompilerBuilder()
                    .setStandardMacros(CelStandardMacro.values())
                    .setContainer("google.rpc.context.AttributeContext")
                    .addMessageTypes(PlayerMessage.getDescriptor().getMessageTypes())
                    .addFunctionDeclarations(
                            TabberProvider.get()
                                    .getFunctionManager()
                                    .getRegisteredFunctions()
                                    .stream()
                                    .map(CelFunction::getFunction)
                                    .collect(Collectors.toList()))
                    .addVar("player", CelTypes.createMessage("Player"))
                    .build();
    private final CelRuntime CEL_RUNTIME =
            CelRuntimeFactory.standardCelRuntimeBuilder()
                    .addFunctionBindings(
                            TabberProvider.get()
                                    .getFunctionManager()
                                    .getRegisteredFunctions()
                                    .stream()
                                    .flatMap(f -> f.getBinding().stream())
                                    .collect(Collectors.toList()))
                    .build();

    @Override
    public @NotNull String getName() {
        return "eval";
    }

    @Override
    public @NotNull String getDescription() {
        return "Evaluates a given expression";
    }

    @Override
    public void execute(@NotNull Audience sender, @NotNull String @NotNull [] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Usage: /tabber eval <player> <expression>"));
            return;
        }
        TabberPlayer player = TabberProvider.get().getPlayer(args[0]).orElse(null);
        if (player == null) {
            sender.sendMessage(Component.text("Player not found!", NamedTextColor.RED));
            return;
        }
        String expression = String.join(" ", args).substring(args[0].length() + 1);
        sender.sendMessage(Component.text("> " + expression, NamedTextColor.GRAY));
        try {
            CelAbstractSyntaxTree ast = CEL_COMPILER.compile(expression).getAst();
            CelRuntime.Program program = CEL_RUNTIME.createProgram(ast);
            Object result = program.eval(Map.of("player", player.toProtobuf()));
            sender.sendMessage(Component.text("Result: ", NamedTextColor.GREEN)
                    .append(Component.text(String.valueOf(result), NamedTextColor.WHITE)));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Error: " + e.getMessage(), NamedTextColor.RED));
            Logger.getCurrentLogger().warn("Error while evaluating expression via command", e);
        }
    }
}
