package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketCreateEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketCreateEvent extends DefaultTicketCancelAbleEvent implements TicketCreateEvent {

    private final DKSupportPlayer creator;

    public DefaultTicketCreateEvent(Ticket ticket, DKSupportPlayer creator) {
        super(ticket);
        this.creator = creator;
    }

    @Override
    public @NotNull DKSupportPlayer getCreator() {
        return this.creator;
    }
}
