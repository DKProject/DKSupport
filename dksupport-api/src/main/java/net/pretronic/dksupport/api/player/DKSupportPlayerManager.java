package net.pretronic.dksupport.api.player;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface DKSupportPlayerManager {

    DKSupportPlayer getPlayer(@NotNull UUID playerId);
}
