package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public interface TicketManager {

    Ticket getTicket(UUID id);

    @NotNull
    Collection<Ticket> getTickets(@NotNull TicketState state);

    Ticket getOpenTicketForCreator(DKSupportPlayer player);


    Ticket createTicket(@NotNull DKSupportPlayer creator);

    boolean deleteTicket(@NotNull Ticket ticket);
}
