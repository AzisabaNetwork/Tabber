package net.azisaba.tabber.api.macro;

import dev.cel.parser.CelMacro;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum TabberMacros {
    GET_PLACEHOLDER(CelMacro.newReceiverMacro(
            "get_placeholder",
            1,
            (exprFactory, target, arguments) -> {
                return Optional.of(exprFactory.newStringLiteral("test"));
            })
    ),
    ;

    private final CelMacro macro;

    TabberMacros(CelMacro macro) {
        this.macro = macro;
    }

    public @NotNull CelMacro getMacro() {
        return macro;
    }
}
