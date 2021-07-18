package net.pretronic.dksupport.common.ticket;

import net.pretronic.dksupport.api.event.ticket.TicketTakeEvent;
import net.pretronic.dksupport.api.event.ticket.TicketUpdateStateEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantAddEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantRemoveEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.common.DefaultDKSupport;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketTakeEvent;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketUpdateStateEvent;
import net.pretronic.dksupport.common.event.ticket.participant.DefaultTicketParticipantAddEvent;
import net.pretronic.dksupport.common.event.ticket.participant.DefaultTicketParticipantMessageEvent;
import net.pretronic.dksupport.common.event.ticket.participant.DefaultTicketParticipantRemoveEvent;
import net.pretronic.libraries.utility.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DefaultTicket implements Ticket {

    private final DefaultDKSupport dkSupport;

    private final UUID id;
    private String topic;
    private TicketState state;
    private Collection<TicketParticipant> participants;
    private List<TicketMessage> messages;
    private final long created;
    private final UUID creatorId;

    public DefaultTicket(@NotNull DefaultDKSupport dkSupport, String topic, UUID creatorId) {
        this(dkSupport, UUID.randomUUID(), topic, TicketState.OPEN, System.currentTimeMillis(), creatorId);
    }

    public DefaultTicket(@NotNull DefaultDKSupport dkSupport, @NotNull UUID id, String topic, @NotNull TicketState state, long created, UUID creatorId) {
        this.dkSupport = dkSupport;
        this.id = id;
        this.topic = topic;
        this.state = state;
        this.created = created;
        this.creatorId = creatorId;
    }

    @Override
    public @NotNull UUID getId() {
        return this.id;
    }

    @Override
    public long getCreated() {
        return this.created;
    }

    @Override
    public TicketParticipant getCreator() {
        return getParticipant(this.creatorId);
    }

    @Override
    public String getTopic() {
        return this.topic;
    }

    @Override
    public boolean setTopic(@NotNull String topic) {
        this.topic = topic;
        this.dkSupport.getStorage().getTickets().update()
                .set("Topic", topic)
                .where("Id", getId())
                .execute();
        return true;//@Todo event
    }

    @Override
    public @NotNull TicketState getState() {
        return this.state;
    }

    @Override
    public boolean setState(@NotNull TicketState state) {
        this.state = state;
        this.dkSupport.getStorage().getTickets().update()
                .set("State", state)
                .where("Id", getId())
                .execute();

        TicketUpdateStateEvent event = new DefaultTicketUpdateStateEvent(this, state);
        this.dkSupport.getEventBus().callEvent(TicketUpdateStateEvent.class, event);
        return true;
    }

    @Override
    public @NotNull Collection<TicketParticipant> getParticipants() {
        return Collections.unmodifiableCollection(getParticipantsOrLoad());
    }

    @Override
    public TicketParticipant getParticipant(@NotNull DKSupportPlayer player) {
        return Iterators.findOne(getParticipantsOrLoad(), participant -> participant.getPlayer().equals(player));
    }

    @Override
    public TicketParticipant getParticipant(@NotNull UUID playerId) {
        return Iterators.findOne(getParticipantsOrLoad(), participant -> participant.getPlayer().getId().equals(playerId));
    }

    @Override
    public TicketParticipant addParticipant(@NotNull DKSupportPlayer player) {
        return addParticipant(player, false, true);
    }

    @Override
    public TicketParticipant addParticipant(@NotNull DKSupportPlayer player, boolean hidden, boolean receiveMessages) {
        if(getParticipant(player) != null) return null;
        TicketParticipant participant = new DefaultTicketParticipant(this, player, hidden,
                System.currentTimeMillis(), receiveMessages);

        TicketParticipantAddEvent event = new DefaultTicketParticipantAddEvent(this, participant);
        this.dkSupport.getEventBus().callEvent(TicketParticipantAddEvent.class, event);
        if(event.isCancelled()) return null;

        this.dkSupport.getStorage().getTicketParticipants().insert()
                .set("TicketId", getId())
                .set("PlayerId", participant.getPlayer().getId())
                .set("Hidden", participant.isHidden())
                .set("Joined", participant.getJoined())
                .set("ReceiveMessages", participant.receiveMessages())
                .execute();

        getParticipantsOrLoad().add(participant);
        return participant;
    }

    @Override
    public TicketParticipant removeParticipant(@NotNull DKSupportPlayer player) {
        TicketParticipant participant = getParticipant(player);
        if(participant == null) throw new IllegalArgumentException("Can't remove player " + player.getId() + " from ticket " + getId() + ". He is not member of this ticket");
        if(removeParticipant(participant)) return participant;
        return null;
    }

    @Override
    public boolean removeParticipant(@NotNull TicketParticipant participant) {
        if(!participant.getTicket().equals(this)) {
            throw new IllegalArgumentException("Ticket participant("+ participant.getPlayer().getId() +","+participant.getTicket().getId()
                    +") is not a participant of ticket " + getId());
        }

        TicketParticipantRemoveEvent event = new DefaultTicketParticipantRemoveEvent(this, participant);
        this.dkSupport.getEventBus().callEvent(TicketParticipantRemoveEvent.class, event);
        if(event.isCancelled()) return false;

        this.dkSupport.getStorage().getTicketParticipants().delete()
                .where("PlayerId", participant.getPlayer().getId())
                .where("TicketId", getId())
                .execute();

        return getParticipantsOrLoad().remove(participant);
    }

    @Override
    public boolean isParticipant(@NotNull DKSupportPlayer player) {
        return getParticipant(player) != null;
    }

    @Override
    public boolean isParticipant(@NotNull UUID playerId) {
        return getParticipant(playerId) != null;
    }

    @Override
    public TicketMessage getLastMessage() {
        TicketMessage message = null;
        for (TicketMessage ticketMessage : getMessagesOrLoad()) {
            if(message == null || message.getTime() < ticketMessage.getTime()) {
                message = ticketMessage;
            }
        }
        return message;
    }

    @Override
    public @NotNull List<TicketMessage> getMessages() {
        return Collections.unmodifiableList(getMessagesOrLoad());
    }

    @Override
    public TicketParticipant take(@NotNull DKSupportPlayer staff) {
        if(this.state != TicketState.OPEN) {
            throw new IllegalArgumentException("Can't take ticket in state " + getState());
        }
        TicketTakeEvent event = new DefaultTicketTakeEvent(this, staff);
        this.dkSupport.getEventBus().callEvent(TicketTakeEvent.class, event);
        if(event.isCancelled()) return null;
        setState(TicketState.PROCESSING);
        return addParticipant(staff);
    }

    @Override
    public TicketMessage sendMessage(@NotNull TicketParticipant sender, @NotNull String message) {
        return sendMessage("minecraft", sender, message);
    }

    @Override
    public TicketMessage sendMessage(@NotNull String source, @NotNull TicketParticipant sender, @NotNull String message) {
        if(!sender.getTicket().equals(this)) {
            throw new IllegalArgumentException("Ticket participant("+ sender.getPlayer().getId() +","+sender.getTicket().getId()
                    +") is not a participant of ticket " + getId());
        }
        long time = System.currentTimeMillis();
        TicketMessage ticketMessage = new DefaultTicketMessage(this, sender.getPlayer(), message, time);
        TicketParticipantMessageEvent event = new DefaultTicketParticipantMessageEvent(this, sender, source, ticketMessage);
        this.dkSupport.getEventBus().callEvent(TicketParticipantMessageEvent.class, event);

        this.dkSupport.getStorage().getTicketMessages().insert()
                .set("TicketId", getId())
                .set("SenderId", sender.getPlayer().getId())
                .set("Message", message)
                .set("Time", time)
                .execute();

        getMessagesOrLoad().add(ticketMessage);

        return ticketMessage;
    }

    private Collection<TicketParticipant> getParticipantsOrLoad() {
        if(this.participants == null) {
            this.participants = new ArrayList<>();
            this.dkSupport.getStorage().getTicketParticipants().find()
                    .where("TicketId", getId())
                    .execute().loadIn(this.participants, resultEntry -> {
                        UUID playerId = resultEntry.getUniqueId("PlayerId");
                        DKSupportPlayer player = this.dkSupport.getPlayerManager().getPlayer(playerId);
                        return new DefaultTicketParticipant(this, player,
                                resultEntry.getBoolean("Hidden"),
                                resultEntry.getLong("Joined"),
                                resultEntry.getBoolean("ReceiveMessages"));
            });
        }
        return this.participants;
    }

    private List<TicketMessage> getMessagesOrLoad() {
        if(this.messages == null) {
            this.messages = new ArrayList<>();
            this.dkSupport.getStorage().getTicketMessages().find()
                    .join(this.dkSupport.getStorage().getTicketParticipants()).on("SenderId", "PlayerId")
                    .where(this.dkSupport.getStorage().getTicketMessages().getName()+".TicketId", getId())
                    .execute().loadIn(this.messages,  resultEntry -> new DefaultTicketMessage(this,
                    this.dkSupport.getPlayerManager().getPlayer(resultEntry.getUniqueId("SenderId")),
                    resultEntry.getString("Message"),
                    resultEntry.getLong("Time")));
        }
        return this.messages;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o instanceof Ticket) {
            return ((Ticket)o).getId().equals(getId());
        }
        return false;
    }
}
