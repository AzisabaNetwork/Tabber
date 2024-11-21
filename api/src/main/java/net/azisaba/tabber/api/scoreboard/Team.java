package net.azisaba.tabber.api.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a team in a scoreboard.
 */
public interface Team {
    /**
     * Get the name of this team.
     * @return the name
     */
    @NotNull String getName();

    /**
     * Get the display name of this team.
     * @return the display name, as a Component
     */
    @NotNull Component getDisplayName();

    /**
     * Add an entry to this team.
     * @param entry the entry to add
     */
    void addEntry(@NotNull String entry);

    /**
     * Remove an entry from this team.
     * @param entry the entry to remove
     */
    void removeEntry(@NotNull String entry);

    /**
     * Set the prefix of this team.
     * @param prefix the prefix to set
     */
    void setPrefix(@NotNull Component prefix);

    /**
     * Get the prefix of this team.
     * @return the prefix
     */
    @NotNull Component getPrefix();

    /**
     * Set the suffix of this team.
     * @param suffix the suffix to set
     */
    void setSuffix(@NotNull Component suffix);

    /**
     * Get the suffix of this team.
     * @return the suffix
     */
    @NotNull Component getSuffix();

    /**
     * Get all entries in this team.
     * @return the entries
     */
    @NotNull Collection<String> getEntries();

    interface Builder {
        @NotNull Builder displayName(@NotNull Component displayName);

        @NotNull Builder prefix(@NotNull Component prefix);

        @NotNull Builder suffix(@NotNull Component suffix);

        @NotNull Builder collisionRule(@NotNull CollisionRule rule);

        @NotNull Builder collisionRule(@NotNull String ruleName);
    }
}
