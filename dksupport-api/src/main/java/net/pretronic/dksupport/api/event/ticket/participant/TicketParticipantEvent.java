package net.pretronic.dksupport.api.event.ticket.participant;

import net.pretronic.dksupport.api.event.ticket.TicketEvent;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import org.jetbrains.annotations.NotNull;

public interface TicketParticipantEvent extends TicketEvent {

    @NotNull
    TicketParticipant getParticipant();
}
