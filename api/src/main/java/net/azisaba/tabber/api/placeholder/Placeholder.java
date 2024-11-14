package net.azisaba.tabber.api.placeholder;

import net.azisaba.tabber.api.actor.TabberPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a placeholder.
 */
public interface Placeholder {
    /**
     * Returns the identifier in this format: %identifier%
     * @return the identifier
     */
    @NotNull String getIdentifier();

    /**
     * Replaces the placeholder in the text with the actual value.
     *
     * @param text   the text to replace
     * @param player the player to get the value
     * @return the replaced text
     */
    String replace(@NotNull String text, @NotNull TabberPlayer player);
}
