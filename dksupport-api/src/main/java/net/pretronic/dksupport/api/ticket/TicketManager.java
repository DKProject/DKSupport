package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface TicketManager {

    @NotNull
    Collection<Ticket> getTickets();

    Ticket getTicket(UUID id);

    Ticket createTicket(@NotNull DKSupportPlayer player);

    boolean deleteTicket(@NotNull Ticket ticket);
}
