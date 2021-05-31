package net.pretronic.dksupport.minecraft.listeners;

import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.event.EventPriority;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.ConnectedMinecraftPlayer;

public class PerformListener {

    @Listener(priority = EventPriority.HIGH, execution = ExecutionType.ASYNC)
    public void onTicketCreate(TicketCreatedEvent event) {
        Ticket ticket = event.getTicket();
        if(ticket.getState() == TicketState.OPEN) {
            VariableSet variables = VariableSet.create().addDescribed("ticket", event.getTicket());
            for (ConnectedMinecraftPlayer player : McNative.getInstance().getLocal().getConnectedPlayers()) {
                if(player.hasSetting("DKSupport", PlayerSettingsKey.SUPPORT,true)) {
                    player.sendMessage(Messages.TICKET_CREATE_STAFF, variables);
                }
            }
        }
    }

    @Listener(execution = ExecutionType.ASYNC)
    public void onParticipantMessage(TicketParticipantMessageEvent event) {
        Ticket ticket = event.getTicket();
        for (ConnectedMinecraftPlayer connectedPlayer : McNative.getInstance().getLocal().getConnectedPlayers()) {
            TicketParticipant participant = ticket.getParticipant(connectedPlayer.getUniqueId());
            if(participant != null && participant.receiveMessages()) {
                connectedPlayer.sendMessage(Messages.TICKET_MESSAGE_RECEIVED, VariableSet.create()
                        .addDescribed("ticket", ticket)
                        .addDescribed("sender", event.getParticipant())
                        .addDescribed("message", event.getMessage()));
            }
        }
    }
}
