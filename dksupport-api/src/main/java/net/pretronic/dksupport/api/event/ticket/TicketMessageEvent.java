package net.pretronic.dksupport.api.event.ticket;

import net.pretronic.dksupport.api.ticket.TicketMessage;
import org.jetbrains.annotations.NotNull;

public interface TicketMessageEvent extends TicketEvent {

    @NotNull
    TicketMessage getMessage();
}
