package net.pretronic.dksupport.common.player;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultDKSupportPlayer implements DKSupportPlayer {

    private final UUID id;

    public DefaultDKSupportPlayer(UUID id) {
        this.id = id;
    }

    @Override
    public @NotNull UUID getId() {
        return this.id;
    }
}
