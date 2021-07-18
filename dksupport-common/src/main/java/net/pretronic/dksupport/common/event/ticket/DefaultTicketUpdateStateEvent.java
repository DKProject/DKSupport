package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketUpdateStateEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketUpdateStateEvent implements TicketUpdateStateEvent {

    private final Ticket ticket;
    private final TicketState newState;

    public DefaultTicketUpdateStateEvent(Ticket ticket, TicketState newState) {
        this.ticket = ticket;
        this.newState = newState;
    }

    @Override
    public @NotNull Ticket getTicket() {
        return this.ticket;
    }

    @Override
    public @NotNull TicketState getNewState() {
        return this.newState;
    }
}
