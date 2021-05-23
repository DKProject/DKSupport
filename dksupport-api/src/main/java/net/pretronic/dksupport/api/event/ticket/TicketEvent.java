package net.pretronic.dksupport.api.event.ticket;

import net.pretronic.dksupport.api.ticket.Ticket;
import org.jetbrains.annotations.NotNull;

public interface TicketEvent {

    @NotNull
    Ticket getTicket();
}
