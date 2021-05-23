package net.pretronic.dksupport.common.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.libraries.utility.Validate;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketParticipant implements TicketParticipant {

    private final DKSupportPlayer player;
    private boolean hidden;
    private final long joined;

    public DefaultTicketParticipant(@NotNull DKSupportPlayer player, boolean hidden, long joined) {
        Validate.isTrue(joined > 0);
        this.player = player;
        this.hidden = hidden;
        this.joined = joined;
    }

    @Override
    public @NotNull DKSupportPlayer getPlayer() {
        return this.player;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public boolean setHidden(boolean hidden) {
        this.hidden = hidden;
        return true;
    }

    @Override
    public long getJoined() {
        return this.joined;
    }
}
