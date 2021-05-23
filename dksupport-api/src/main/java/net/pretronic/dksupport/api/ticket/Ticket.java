package net.pretronic.dksupport.api.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface Ticket {

    @NotNull
    UUID getId();


    @NotNull
    String getCategory();

    void setCategory(@NotNull String category);


    @NotNull
    TicketState getState();

    boolean setState(@NotNull TicketState state);


    @NotNull
    Collection<TicketParticipant> getParticipants();

    TicketParticipant getParticipant(@NotNull DKSupportPlayer player);

    TicketParticipant addParticipant(@NotNull DKSupportPlayer player, boolean hidden);

    boolean removeParticipant(@NotNull TicketParticipant participant);

    boolean isParticipant(@NotNull DKSupportPlayer player);


    TicketMessage getLastMessage();

    @NotNull
    List<TicketMessage> getMessages();


    boolean take(@NotNull DKSupportPlayer stuff);
    

    TicketMessage sendMessage(@NotNull TicketParticipant sender, @NotNull String message);
}
