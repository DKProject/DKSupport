package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

public interface TicketMessage {

    @NotNull
    Ticket getTicket();

    @NotNull
    DKSupportPlayer getSender();

    TicketParticipant getSenderAsParticipant();

    @NotNull
    String getText();

    long getTime();
}
