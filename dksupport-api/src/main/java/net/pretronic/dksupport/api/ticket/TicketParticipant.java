package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

public interface TicketParticipant {

    @NotNull
    DKSupportPlayer getPlayer();

    boolean isHidden();

    boolean setHidden(boolean hidden);

    long getJoined();
}
