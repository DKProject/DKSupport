package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TicketManager {

    @NotNull
    Collection<Ticket> getTickets();

    Ticket createTicket(@NotNull DKSupportPlayer player);

    boolean deleteTicket(@NotNull Ticket ticket);
}
