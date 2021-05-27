package net.pretronic.dksupport.common.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.libraries.utility.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DefaultTicketParticipant implements TicketParticipant {

    private final Ticket ticket;
    private final DKSupportPlayer player;
    private boolean hidden;
    private final long joined;
    private boolean receiveMessages;

    public DefaultTicketParticipant(Ticket ticket, @NotNull DKSupportPlayer player, boolean hidden, long joined, boolean receiveMessages) {
        this.ticket = ticket;
        this.receiveMessages = receiveMessages;
        Validate.isTrue(joined > 0);
        this.player = player;
        this.hidden = hidden;
        this.joined = joined;
    }

    @Override
    public @NotNull Ticket getTicket() {
        return this.ticket;
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

    @Override
    public boolean receiveMessages() {
        return this.receiveMessages;
    }

    @Override
    public boolean setReceiveMessages(boolean receiveMessages) {
        this.receiveMessages = receiveMessages;
        return true;
    }
}
