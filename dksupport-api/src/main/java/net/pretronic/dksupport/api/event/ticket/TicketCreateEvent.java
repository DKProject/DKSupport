package net.pretronic.dksupport.api.event.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.libraries.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public interface TicketCreateEvent extends Cancellable {

    @NotNull
    DKSupportPlayer getCreator();

    String getTopic();
}
