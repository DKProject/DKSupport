package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketDeleteEvent;
import net.pretronic.dksupport.api.ticket.Ticket;

public class DefaultTicketDeleteEvent extends DefaultTicketCancelAbleEvent implements TicketDeleteEvent {

    public DefaultTicketDeleteEvent(Ticket ticket) {
        super(ticket);
    }
}
