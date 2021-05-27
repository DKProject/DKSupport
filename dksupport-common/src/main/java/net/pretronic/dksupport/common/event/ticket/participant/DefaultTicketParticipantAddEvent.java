package net.pretronic.dksupport.common.event.ticket.participant;

import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantAddEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;

public class DefaultTicketParticipantAddEvent extends DefaultTicketParticipantCancelAbleEvent implements TicketParticipantAddEvent {

    public DefaultTicketParticipantAddEvent(Ticket ticket, TicketParticipant participant) {
        super(ticket, participant);
    }
}
