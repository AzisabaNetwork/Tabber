package net.azisaba.tabber.velocity.placeholder;

import me.neznamy.tab.shared.placeholders.types.TabPlaceholder;
import net.azisaba.tabber.api.actor.TabberPlayer;
import net.azisaba.tabber.api.placeholder.Placeholder;
import net.azisaba.tabber.velocity.actor.VelocityTabberPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record PlaceholderImpl(@NotNull me.neznamy.tab.api.placeholder.Placeholder tabPlaceholder) implements Placeholder {
    public PlaceholderImpl(@NotNull me.neznamy.tab.api.placeholder.Placeholder tabPlaceholder) {
        this.tabPlaceholder = Objects.requireNonNull(tabPlaceholder);
    }

    @Override
    public @NotNull String getIdentifier() {
        return tabPlaceholder.getIdentifier();
    }

    @Override
    public String replace(@NotNull String text, @NotNull TabberPlayer player) {
        if (!(player instanceof VelocityTabberPlayer)) {
            throw new IllegalArgumentException("player is not an instance of VelocityTabberPlayer");
        }
        if (!(this.tabPlaceholder instanceof TabPlaceholder)) {
            throw new IllegalArgumentException("tabPlaceholder is not an instance of TabPlaceholder");
        }
        return ((TabPlaceholder) tabPlaceholder).set(text, ((VelocityTabberPlayer) player).getTabPlayer());
    }
}
