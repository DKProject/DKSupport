package net.pretronic.dksupport.common.event.ticket.participant;

import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantRemoveEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;

public class DefaultTicketParticipantRemoveEvent extends DefaultTicketParticipantCancelAbleEvent implements TicketParticipantRemoveEvent {

    public DefaultTicketParticipantRemoveEvent(Ticket ticket, TicketParticipant participant) {
        super(ticket, participant);
    }
}
