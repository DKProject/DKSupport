package net.pretronic.dksupport.common.event.ticket.participant;

import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.libraries.event.Cancellable;

public class DefaultTicketParticipantCancelAbleEvent extends DefaultTicketParticipantEvent implements Cancellable {

    private boolean cancelled;

    public DefaultTicketParticipantCancelAbleEvent(Ticket ticket, TicketParticipant participant) {
        super(ticket, participant);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
