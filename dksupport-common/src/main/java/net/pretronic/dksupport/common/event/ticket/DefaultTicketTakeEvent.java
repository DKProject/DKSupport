package net.pretronic.dksupport.common.event.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketTakeEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;

public class DefaultTicketTakeEvent extends DefaultTicketCancelAbleEvent implements TicketTakeEvent {

    private final DKSupportPlayer staff;

    public DefaultTicketTakeEvent(Ticket ticket, DKSupportPlayer staff) {
        super(ticket);
        this.staff = staff;
    }

    @Override
    public DKSupportPlayer getStaff() {
        return this.staff;
    }
}
