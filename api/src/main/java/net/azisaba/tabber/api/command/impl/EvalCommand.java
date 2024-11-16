package net.azisaba.tabber.api.command.impl;

import dev.cel.common.CelAbstractSyntaxTree;
import dev.cel.runtime.CelRuntime;
import net.azisaba.tabber.api.Logger;
import net.azisaba.tabber.api.TabberProvider;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.command.Command;
import net.azisaba.tabber.api.order.OrderData;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EvalCommand implements Command {
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
            long start = System.currentTimeMillis();
            CelAbstractSyntaxTree ast = OrderData.CEL_COMPILER.get().compile(expression).getAst();
            CelRuntime.Program program = OrderData.CEL_RUNTIME.get().createProgram(ast);
            Object result = program.eval(Map.of("player", player.toProtobuf()));
            long end = System.currentTimeMillis();
            sender.sendMessage(Component.text("Time: " + (end - start) + " ms", NamedTextColor.GRAY));
            sender.sendMessage(Component.text("Result: ", NamedTextColor.GREEN)
                    .append(Component.text(String.valueOf(result), NamedTextColor.WHITE)));
        } catch (Exception e) {
            sender.sendMessage(Component.text("Error: " + e.getMessage(), NamedTextColor.RED));
            Logger.getCurrentLogger().warn("Error while evaluating expression via command", e);
        }
    }
}
