package net.pretronic.dksupport.minecraft.commands;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.dksupport.minecraft.config.TicketTopic;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Convert;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class CommandUtil {

    public static boolean isConsole(CommandSender sender){
        if(!(sender instanceof OnlineMinecraftPlayer)) {
            sender.sendMessage(Messages.ERROR_ONLY_PLAYER);
            return true;
        }
        return false;
    }

    public static void changeLogin(String settingKey, OnlineMinecraftPlayer player, boolean current, boolean action){
        if(current == action){
            player.sendMessage(Messages.STAFF_STATUS_ALREADY, VariableSet.create()
                    .add("status",action)
                    .add("statusFormatted", action ? Messages.STAFF_STATUS_LOGIN :  Messages.STAFF_STATUS_LOGOUT));
        }else{
            player.sendMessage(Messages.STAFF_STATUS_CHANGE, VariableSet.create()
                    .add("status",action)
                    .add("statusFormatted", action ? Messages.STAFF_STATUS_LOGIN :  Messages.STAFF_STATUS_LOGOUT));
            player.setSetting("DKSupport",settingKey,action);
        }
    }

    public static void sendTicketHelpMessage(CommandSender sender) {
        VariableSet variables = VariableSet.create().addDescribed("topics", DKSupportConfig.TICKET_TOPICS);
        if((sender instanceof MinecraftPlayer && ((MinecraftPlayer)sender).hasPermission(Permissions.STAFF))
                || McNative.getInstance().getConsoleSender().equals(sender)) {
            sender.sendMessage(Messages.COMMAND_TICKET_HELP_STAFF, variables);
        } else {
            sender.sendMessage(Messages.COMMAND_TICKET_HELP_USER, variables);
        }
    }

    public static String readStringFromArguments(String[] arguments, int start){
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (int i = start; i < arguments.length; i++){
            if(first) {
                first = false;
                builder.append(' ');
            }
            builder.append(arguments[i]);
        }
        return builder.toString();
    }

    public static void setSelectedTicket(MinecraftPlayer player, UUID ticketId) {
        player.setSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED, ticketId);
    }

    public static Ticket getSelectedTicket(DKSupport dkSupport, OnlineMinecraftPlayer player) {
        return getSelectedTicket(dkSupport, player, true);
    }

    public static Ticket getSelectedTicket(DKSupport dkSupport, OnlineMinecraftPlayer player, boolean sendMessage) {
        if(player.hasSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED)) {
            Setting setting = player.getSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
            UUID ticketId = Convert.toUUID(setting.getObjectValue());
            Ticket ticket = dkSupport.getTicketManager().getTicket(Convert.toUUID(ticketId));
            if(ticket == null || ticket.getState() == TicketState.CLOSED) {
                player.removeSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
                if(sendMessage) player.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
                return null;
            }
            if(ticket.getState() == TicketState.CLOSED) {
                player.removeSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
                if(sendMessage) player.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
                return null;
            }
            return ticket;
        } else {
            if(sendMessage) player.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
            return null;
        }
    }

    public static void unselectTicket(MinecraftPlayer player) {
        player.removeSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
    }
}
