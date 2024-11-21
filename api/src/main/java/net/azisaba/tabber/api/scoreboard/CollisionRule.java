package net.azisaba.tabber.api.scoreboard;

import org.jetbrains.annotations.NotNull;

public enum CollisionRule {
    ALWAYS("always"),
    NEVER("never"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam");

    private final String name;

    CollisionRule(String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public static @NotNull CollisionRule getByName(@NotNull String name) {
        for (CollisionRule rule : values()) {
            if (rule.getName().equalsIgnoreCase(name) || rule.name().equalsIgnoreCase(name)) {
                return rule;
            }
        }
        throw new IllegalArgumentException("Unknown CollisionRule: " + name);
    }
}
