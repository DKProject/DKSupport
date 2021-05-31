package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.ticket.Ticket;

public class DefaultTicketCreatedEvent extends DefaultTicketCancelAbleEvent implements TicketCreatedEvent {

    public DefaultTicketCreatedEvent(Ticket ticket) {
        super(ticket);
    }
}
