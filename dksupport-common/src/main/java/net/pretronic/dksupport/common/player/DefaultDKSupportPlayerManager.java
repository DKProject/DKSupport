package net.pretronic.dksupport.common.player;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.player.DKSupportPlayerManager;
import net.pretronic.dksupport.common.DefaultDKSupport;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultDKSupportPlayerManager implements DKSupportPlayerManager {

    private final DefaultDKSupport dkSupport;

    public DefaultDKSupportPlayerManager(DefaultDKSupport dkSupport) {
        this.dkSupport = dkSupport;
    }

    @Override
    public DKSupportPlayer getPlayer(@NotNull UUID playerId) {
        return new DefaultDKSupportPlayer(playerId);//@Todo maybe caching
    }
}
