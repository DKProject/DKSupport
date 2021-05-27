package net.pretronic.dksupport.common.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketMessage implements TicketMessage {

    private final Ticket ticket;
    private final DKSupportPlayer sender;
    private final String text;
    private final long time;

    public DefaultTicketMessage(Ticket ticket, DKSupportPlayer sender, String text, long time) {
        this.ticket = ticket;
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    @Override
    public @NotNull Ticket getTicket() {
        return this.ticket;
    }

    @Override
    public @NotNull DKSupportPlayer getSender() {
        return this.sender;
    }

    @Override
    public TicketParticipant getSenderAsParticipant() {
        return this.ticket.getParticipant(sender);
    }

    @Override
    public @NotNull String getText() {
        return this.text;
    }

    @Override
    public long getTime() {
        return this.time;
    }
}
