package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.libraries.event.Cancellable;

public class DefaultTicketCancelAbleEvent extends DefaultTicketEvent implements Cancellable {

    private boolean cancelled;

    public DefaultTicketCancelAbleEvent(Ticket ticket) {
        super(ticket);
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
