package net.pretronic.dksupport.minecraft.listeners;

import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.TicketTakeEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.DKSupportPlugin;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.integration.DKConnectIntegration;
import net.pretronic.libraries.event.EventPriority;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.event.service.registry.ServiceRegisterRegistryEvent;
import org.mcnative.runtime.api.player.ConnectedMinecraftPlayer;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class PerformListener {

    private final DKSupportPlugin plugin;

    public PerformListener(DKSupportPlugin plugin) {
        this.plugin = plugin;
    }

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
                        .addDescribed("message", event.getMessage()));
            }
        }
    }

    @Listener(priority = EventPriority.HIGHEST)
    public void onTicketTake(TicketTakeEvent event) {
        if(event.isCancelled()) return;
        VariableSet variables = VariableSet.create().addDescribed("ticket", event.getTicket()).addDescribed("staff", event.getStaff());

        MinecraftPlayer staff = McNative.getInstance().getPlayerManager().getPlayer(event.getStaff().getId());
        if(staff.isOnline()) {
            staff.getAsOnlinePlayer().sendMessage(Messages.TICKET_TAKE_STAFF, variables);
        }
        MinecraftPlayer creator = McNative.getInstance().getPlayerManager().getPlayer(event.getTicket().getCreator().getPlayer().getId());
        if(creator.isOnline()) {
            creator.getAsOnlinePlayer().sendMessage(Messages.TICKET_TAKE_CREATOR, variables);
        }
    }

    @Listener
    public void onServiceRegister(ServiceRegisterRegistryEvent event) {
        if(DKSupportConfig.DKCONNECT_INTEGRATION_ENABLED && event.getClass().getName().equals("net.pretronic.dkconnect.api.DKConnect") && this.plugin.getDKConnectIntegration() == null) {
            this.plugin.getLogger().info("Enabling DKConnect integration");
            new DKConnectIntegration(this.plugin, dkSupport, (net.pretronic.dkconnect.api.DKConnect) event.getService());
        }
    }
}
