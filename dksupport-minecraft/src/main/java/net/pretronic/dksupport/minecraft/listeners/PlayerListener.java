package net.pretronic.dksupport.minecraft.listeners;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.event.EventPriority;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.event.player.MinecraftPlayerChatEvent;
import org.mcnative.runtime.api.event.player.MinecraftPlayerLogoutEvent;
import org.mcnative.runtime.api.event.player.login.MinecraftPlayerPostLoginEvent;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class PlayerListener {

    private final DKSupport dkSupport;

    public PlayerListener(DKSupport dkSupport) {
        this.dkSupport = dkSupport;
    }

    @Listener(execution = ExecutionType.ASYNC)
    public void onJoin(MinecraftPlayerPostLoginEvent event){
        OnlineMinecraftPlayer player = event.getOnlinePlayer();
        if(DKSupportConfig.PLAYER_ON_LOGIN_INFO){
            if(player.hasPermission(DKSupportConfig.PERMISSION_STAFF)) {
                boolean support = event.getPlayer().hasSetting("DKSupport", PlayerSettingsKey.SUPPORT,true);
                player.sendMessage(Messages.STAFF_STATUS_NOW, VariableSet.create()
                        .add("status",support)
                        .add("statusFormatted", support ? Messages.STAFF_STATUS_LOGIN :  Messages.STAFF_STATUS_LOGOUT));
                player.sendMessage(Messages.STAFF_JOIN_INFO, VariableSet.create().add("openTicketCount", dkSupport.getTicketManager().getTickets(TicketState.OPEN).size()));
            } else if(dkSupport.getTicketManager().getTicketForCreator(player.getAs(DKSupportPlayer.class), TicketState.PROCESSING) != null) {
                player.sendMessage(Messages.USER_JOIN_INFO);
            }
        }
    }

    @Listener(execution = ExecutionType.ASYNC)
    public void onLogout(MinecraftPlayerLogoutEvent event) {
        CommandUtil.unselectTicket(event.getPlayer());
    }

    @Listener(priority = EventPriority.HIGH)
    public void onChat(MinecraftPlayerChatEvent event) {
        if(event.isCancelled()) return;
        OnlineMinecraftPlayer player = event.getOnlinePlayer();
        for (Ticket ticket : this.dkSupport.getTicketManager().getTickets(TicketState.PROCESSING)) {
            TicketParticipant participant = ticket.getParticipant(player.getUniqueId());
            if(participant != null && CommandUtil.getSelectedTicket(dkSupport, player, false) != null){
                event.setCancelled(true);
                if(event.getMessage().equalsIgnoreCase("#leave")) {
                    player.performCommand("ticket unselect");
                } else if(event.getMessage().equalsIgnoreCase("#close")) {
                    player.performCommand("ticket close");
                } else if(event.getMessage().equalsIgnoreCase("#history")) {
                    player.performCommand("ticket history");
                } else {
                    ticket.sendMessage(participant, event.getMessage());
                }
                break;
            }
        }
    }
}
