package net.pretronic.dksupport.common.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.libraries.utility.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class DefaultTicket implements Ticket {

    private final UUID id;
    private String category;
    private TicketState state;
    private final Collection<TicketParticipant> participants;
    private final List<TicketMessage> messages;

    public DefaultTicket(@NotNull UUID id, @NotNull String category, @NotNull TicketState state, @NotNull Collection<TicketParticipant> participants,
                         @NotNull List<TicketMessage> messages) {
        this.id = id;
        this.category = category;
        this.state = state;
        this.participants = participants;
        this.messages = messages;
    }

    @Override
    public @NotNull UUID getId() {
        return this.id;
    }

    @Override
    public @NotNull String getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(@NotNull String category) {
        this.category = category;
    }

    @Override
    public @NotNull TicketState getState() {
        return this.state;
    }

    @Override
    public boolean setState(@NotNull TicketState state) {
        this.state = state;
        return true;
    }

    @Override
    public @NotNull Collection<TicketParticipant> getParticipants() {
        return Collections.unmodifiableCollection(this.participants);
    }

    @Override
    public TicketParticipant getParticipant(@NotNull DKSupportPlayer player) {
        return Iterators.findOne(this.participants, participant -> participant.getPlayer().equals(player));
    }

    @Override
    public TicketParticipant addParticipant(@NotNull DKSupportPlayer player, boolean hidden) {
        if(getParticipant(player) != null) return null;
        TicketParticipant participant = new DefaultTicketParticipant(player, hidden, System.currentTimeMillis());
        this.participants.add(participant);
        return participant;
    }

    @Override
    public boolean removeParticipant(@NotNull TicketParticipant participant) {
        return false;
    }

    @Override
    public boolean isParticipant(@NotNull DKSupportPlayer player) {
        return false;
    }

    @Override
    public TicketMessage getLastMessage() {
        return null;
    }

    @Override
    public @NotNull List<TicketMessage> getMessages() {
        return Collections.unmodifiableList(this.messages);
    }

    @Override
    public boolean take(@NotNull DKSupportPlayer stuff) {
        return false;
    }

    @Override
    public TicketMessage sendMessage(@NotNull TicketParticipant sender, @NotNull String message) {
        return null;
    }
}
