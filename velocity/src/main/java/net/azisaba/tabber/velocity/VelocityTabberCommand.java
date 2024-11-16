package net.azisaba.tabber.velocity;

import com.velocitypowered.api.command.RawCommand;
import net.azisaba.tabber.api.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VelocityTabberCommand implements RawCommand {
    private final List<Command> commands = new ArrayList<>();

    public void registerCommand(@NotNull Command command) {
        commands.add(command);
    }

    public void unregisterAllCommands() {
        commands.clear();
    }

    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments().split(" ", -1);
        if (args.length == 0) {
            return;
        }
        commands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .ifPresent(command -> command.execute(invocation.source(), Arrays.copyOfRange(args, 1, args.length)));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments().split(" ", -1);
        if (args.length == 0) {
            return commands.stream()
                    .map(Command::getName)
                    .toList();
        } else if (args.length == 1) {
            return commands.stream()
                    .map(Command::getName)
                    .filter(name -> name.startsWith(args[0]))
                    .toList();
        } else {
            return commands.stream()
                    .filter(command -> command.getName().equalsIgnoreCase(args[0]))
                    .findFirst()
                    .map(command -> command.getSuggestions(invocation.source(), Arrays.copyOfRange(args, 1, args.length)))
                    .orElse(List.of());
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        String[] args = invocation.arguments().split(" ", -1);
        if (args.length == 0) {
            return invocation.source().hasPermission("tabber.command");
        }
        return invocation.source().hasPermission("tabber.command." + args[0])
                && invocation.source().hasPermission("tabber.command");
    }
}
