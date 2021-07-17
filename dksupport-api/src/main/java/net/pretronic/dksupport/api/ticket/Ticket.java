package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface Ticket {

    @NotNull
    UUID getId();


    long getCreated();


    TicketParticipant getCreator();


    String getTopic();

    boolean setTopic(@NotNull String topic);




    @NotNull
    TicketState getState();

    boolean setState(@NotNull TicketState state);


    @NotNull
    Collection<TicketParticipant> getParticipants();

    TicketParticipant getParticipant(@NotNull DKSupportPlayer player);

    TicketParticipant getParticipant(@NotNull UUID playerId);

    TicketParticipant addParticipant(@NotNull DKSupportPlayer player);

    TicketParticipant addParticipant(@NotNull DKSupportPlayer player, boolean hidden, boolean receiveMessages);

    TicketParticipant removeParticipant(@NotNull DKSupportPlayer player);

    boolean removeParticipant(@NotNull TicketParticipant participant);

    boolean isParticipant(@NotNull DKSupportPlayer player);

    boolean isParticipant(@NotNull UUID playerId);


    TicketMessage getLastMessage();

    @NotNull
    List<TicketMessage> getMessages();


    TicketParticipant take(@NotNull DKSupportPlayer staff);


    TicketMessage sendMessage(@NotNull TicketParticipant sender, @NotNull String message);

    TicketMessage sendMessage(@NotNull String source, @NotNull TicketParticipant sender, @NotNull String message);
}
