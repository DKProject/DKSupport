package net.pretronic.dksupport.api.event.ticket.participant;

import net.pretronic.dksupport.api.ticket.TicketMessage;
import org.jetbrains.annotations.NotNull;

public interface TicketParticipantMessageEvent extends TicketParticipantEvent {

    @NotNull
    TicketMessage getMessage();
}
