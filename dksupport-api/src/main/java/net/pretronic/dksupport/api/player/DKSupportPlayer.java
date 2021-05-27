package net.pretronic.dksupport.api.player;

import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface DKSupportPlayer {

    @NotNull
    UUID getId();

    Collection<Ticket> getTickets(TicketState state);
}
