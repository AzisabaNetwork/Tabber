package net.azisaba.tabber.api.actor;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TabberPlayer extends Audience {
    @NotNull
    UUID getUniqueId();

    @NotNull
    String getUsername();
}
