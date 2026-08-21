package net.azisaba.tabber.velocity.placeholder;

import me.neznamy.tab.shared.TAB;
import net.azisaba.tabber.api.placeholder.Placeholder;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlaceholderManagerImpl implements PlaceholderManager {
    private static @NotNull Optional<me.neznamy.tab.shared.features.PlaceholderManagerImpl> getTabPlaceholderManager() {
        TAB tab = TAB.getInstance();
        if (tab.isPluginDisabled()) {
            return Optional.empty();
        }
        return Optional.ofNullable(tab.getPlaceholderManager());
    }

    /**
     * Wraps a TAB placeholder into a Tabber placeholder.
     * @param tabPlaceholder the TAB placeholder
     * @return the Tabber placeholder
     */
    @Contract("null -> null; !null -> !null")
    private static Placeholder wrap(me.neznamy.tab.api.placeholder.Placeholder tabPlaceholder) {
        if (tabPlaceholder == null) {
            return null;
        }
        return new PlaceholderImpl(tabPlaceholder);
    }

    @Override
    public @NotNull List<@NotNull Placeholder> getKnownPlaceholders() {
        return getTabPlaceholderManager()
                .map(manager -> manager.getAllPlaceholders()
                        .stream()
                        .map(PlaceholderManagerImpl::wrap)
                        .filter(Objects::nonNull)
                        .toList())
                .orElseGet(List::of);
    }

    @Override
    public @NotNull Optional<@NotNull Placeholder> getPlaceholderByIdentifier(@NotNull String identifier) {
        return getTabPlaceholderManager()
                .map(manager -> manager.getPlaceholderRaw(identifier))
                .map(PlaceholderManagerImpl::wrap);
    }

    @Override
    public @NotNull List<@NotNull String> detectPlaceholders(@NotNull String text) {
        return me.neznamy.tab.shared.features.PlaceholderManagerImpl.detectPlaceholders(text);
    }
}
