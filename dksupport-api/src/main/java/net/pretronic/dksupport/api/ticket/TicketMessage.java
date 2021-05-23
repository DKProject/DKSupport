package net.pretronic.dksupport.api.ticket;

import org.jetbrains.annotations.NotNull;

public interface TicketMessage {

    @NotNull
    TicketParticipant getSender();

    @NotNull
    String getMessage();

    long getTime();
}
