package net.pretronic.dksupport.common.event.ticket.participant;

import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketParticipantMessageEvent extends DefaultTicketParticipantEvent implements TicketParticipantMessageEvent {

    private final String source;
    private final TicketMessage message;

    public DefaultTicketParticipantMessageEvent(Ticket ticket, TicketParticipant participant, String source, TicketMessage message) {
        super(ticket, participant);
        this.source = source;
        this.message = message;
    }

    @Override
    public @NotNull TicketMessage getMessage() {
        return this.message;
    }

    @Override
    public @NotNull String getSource() {
        return this.source;
    }
}
