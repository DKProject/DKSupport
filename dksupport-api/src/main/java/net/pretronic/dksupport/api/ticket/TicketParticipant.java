package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TicketParticipant {

    @NotNull
    Ticket getTicket();

    @NotNull
    DKSupportPlayer getPlayer();

    boolean isHidden();

    boolean setHidden(boolean hidden);

    long getJoined();

    boolean receiveMessages();

    boolean setReceiveMessages(boolean receiveMessages);
}
