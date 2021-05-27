package net.pretronic.dksupport.minecraft.commands;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

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
        if((sender instanceof MinecraftPlayer && ((MinecraftPlayer)sender).hasPermission(Permissions.STAFF))
                || McNative.getInstance().getConsoleSender().equals(sender)) {
            sender.sendMessage(Messages.COMMAND_TICKET_HELP_STAFF);
        } else {
            sender.sendMessage(Messages.COMMAND_TICKET_HELP_USER);
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
}
