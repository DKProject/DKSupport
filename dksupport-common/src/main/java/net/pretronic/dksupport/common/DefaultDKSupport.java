package net.pretronic.dksupport.common;

import net.pretronic.databasequery.api.Database;
import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayerManager;
import net.pretronic.dksupport.api.ticket.TicketManager;
import net.pretronic.dksupport.common.player.DefaultDKSupportPlayerManager;
import net.pretronic.dksupport.common.ticket.DefaultTicketManager;
import net.pretronic.libraries.event.EventBus;
import net.pretronic.libraries.utility.annonations.Internal;
import org.jetbrains.annotations.NotNull;

public class DefaultDKSupport implements DKSupport {

    private final EventBus eventBus;
    private final TicketManager ticketManager;
    private final DKSupportPlayerManager playerManager;

    private final DKSupportStorage storage;

    public DefaultDKSupport(@NotNull EventBus eventBus, @NotNull Database database) {
        this.eventBus = eventBus;
        this.storage = new DKSupportStorage(database);

        this.ticketManager = new DefaultTicketManager(this);
        this.playerManager = new DefaultDKSupportPlayerManager(this);
    }

    @Override
    public @NotNull EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public @NotNull TicketManager getTicketManager() {
        return this.ticketManager;
    }

    @Override
    public @NotNull DKSupportPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Internal
    public DKSupportStorage getStorage() {
        return storage;
    }
}
