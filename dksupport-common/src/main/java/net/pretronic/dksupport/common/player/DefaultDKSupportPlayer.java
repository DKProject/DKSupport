package net.pretronic.dksupport.common.player;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.UUID;

public class DefaultDKSupportPlayer implements DKSupportPlayer {

    private final DKSupport dkSupport;

    private final UUID id;

    public DefaultDKSupportPlayer(DKSupport dkSupport, UUID id) {
        this.dkSupport = dkSupport;
        this.id = id;
    }

    @Override
    public @NotNull UUID getId() {
        return this.id;
    }

    @Override
    public Collection<Ticket> getTickets(TicketState state) {
        return this.dkSupport.getTicketManager().getTickets(this, state);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o instanceof DKSupportPlayer) {
            return ((DKSupportPlayer)o).getId().equals(getId());
        }
        return false;
    }
}
