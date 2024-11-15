package net.azisaba.tabber.api.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Optional;

public interface PlaceholderManager {
    /**
     * Returns all known placeholders. Plugin should not attempt to modify this list.
     * @return all known placeholders
     */
    @Unmodifiable
    @NotNull List<@NotNull Placeholder> getKnownPlaceholders();

    /**
     * Returns a placeholder by its identifier. Returns null if the placeholder is not found.
     * The identifier should be in this format: %identifier%
     * @param identifier the identifier of the placeholder
     * @return the placeholder or null if not found
     */
    @NotNull Optional<@NotNull Placeholder> getPlaceholderByIdentifier(@NotNull String identifier);

    /**
     * Try to detect placeholders in the given text.
     */
    @NotNull List<@NotNull String> detectPlaceholders(@NotNull String text);
}
