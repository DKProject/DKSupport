package net.pretronic.dksupport.minecraft.commands;

import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
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
            player.setSetting("DKBans",settingKey,action);
        }
    }

}
