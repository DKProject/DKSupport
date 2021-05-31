package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketCreateEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

public class DefaultTicketCreateEvent implements TicketCreateEvent {

    private final DKSupportPlayer creator;
    private final String topic;
    private boolean cancelled;

    public DefaultTicketCreateEvent(DKSupportPlayer creator, String topic) {
        this.creator = creator;
        this.topic = topic;
    }

    @Override
    public @NotNull DKSupportPlayer getCreator() {
        return this.creator;
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
