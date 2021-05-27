package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketEvent implements TicketEvent {

    private final Ticket ticket;

    public DefaultTicketEvent(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public @NotNull Ticket getTicket() {
        return this.ticket;
    }
}
