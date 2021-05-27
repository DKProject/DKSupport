package net.pretronic.dksupport.common.event.ticket.participant;

import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketEvent;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketParticipantEvent extends DefaultTicketEvent implements TicketParticipantEvent {

    private final TicketParticipant participant;

    public DefaultTicketParticipantEvent(Ticket ticket, TicketParticipant participant) {
        super(ticket);
        this.participant = participant;
    }

    @Override
    public @NotNull TicketParticipant getParticipant() {
        return this.participant;
    }
}
