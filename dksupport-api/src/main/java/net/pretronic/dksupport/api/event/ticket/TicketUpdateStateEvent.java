package net.pretronic.dksupport.api.event.ticket;

import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.libraries.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public interface TicketUpdateStateEvent extends TicketEvent {

    @NotNull
    TicketState getNewState();
}
