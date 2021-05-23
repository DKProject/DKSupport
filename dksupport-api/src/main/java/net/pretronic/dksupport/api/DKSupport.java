package net.pretronic.dksupport.api;

import net.pretronic.dksupport.api.player.DKSupportPlayerManager;
import net.pretronic.dksupport.api.ticket.TicketManager;
import net.pretronic.libraries.event.EventBus;
import org.jetbrains.annotations.NotNull;

public interface DKSupport {

    @NotNull
    EventBus getEventBus();

    @NotNull
    TicketManager getTicketManager();

    @NotNull
    DKSupportPlayerManager getPlayerManager();
}
