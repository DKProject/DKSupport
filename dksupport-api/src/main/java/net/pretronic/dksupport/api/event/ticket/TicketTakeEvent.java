package net.pretronic.dksupport.api.event.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.libraries.event.Cancellable;

public interface TicketTakeEvent extends TicketEvent, Cancellable {

    DKSupportPlayer getStaff();
}
