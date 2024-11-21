package net.azisaba.tabber.velocity.placeholder;

import me.neznamy.tab.shared.TAB;
import net.azisaba.tabber.api.placeholder.Placeholder;
import net.azisaba.tabber.api.placeholder.PlaceholderManager;
import net.azisaba.tabber.api.util.LazyValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PlaceholderManagerImpl implements PlaceholderManager {
    public final LazyValue<me.neznamy.tab.shared.features.PlaceholderManagerImpl> tabPlaceholderManager =
            new LazyValue<>(() -> TAB.getInstance().getPlaceholderManager());

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
        return tabPlaceholderManager.get()
                .getAllPlaceholders()
                .stream()
                .map(PlaceholderManagerImpl::wrap)
                .toList();
    }

    @Override
    public @NotNull Optional<@NotNull Placeholder> getPlaceholderByIdentifier(@NotNull String identifier) {
        return Optional.ofNullable(wrap(tabPlaceholderManager.get().getPlaceholderRaw(identifier)));
    }

    @Override
    public @NotNull List<@NotNull String> detectPlaceholders(@NotNull String text) {
        return me.neznamy.tab.shared.features.PlaceholderManagerImpl.detectPlaceholders(text);
    }
}
