package net.pretronic.dksupport.api.ticket;

import org.jetbrains.annotations.NotNull;

public enum TicketState {

    OPEN(),
    PROCESSING(),
    CLOSED();

    public static TicketState parseSilent(String value) {
        for (TicketState ticketState : values()) {
            if(ticketState.name().equalsIgnoreCase(value)) return ticketState;
        }
        return null;
    }

    public static TicketState parse(@NotNull String value) {
        TicketState state = parseSilent(value);
        if(state == null) throw new IllegalArgumentException("Can't match TicketState for " + value);
        return state;
    }
}
