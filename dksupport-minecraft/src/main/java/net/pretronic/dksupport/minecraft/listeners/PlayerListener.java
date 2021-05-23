package net.pretronic.dksupport.minecraft.listeners;

import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.event.execution.ExecutionType;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import org.mcnative.runtime.api.event.player.login.MinecraftPlayerPostLoginEvent;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class PlayerListener {

    @Listener(execution = ExecutionType.ASYNC)
    public void onJoin(MinecraftPlayerPostLoginEvent event){
        OnlineMinecraftPlayer player = event.getOnlinePlayer();
        if(DKSupportConfig.PLAYER_ON_LOGIN_INFO && player.hasPermission(DKSupportConfig.PERMISSION_STAFF)){
            boolean teamChat = event.getPlayer().hasSetting("DKSupport", PlayerSettingsKey.SUPPORT,true);
            player.sendMessage(Messages.STAFF_STATUS_NOW, VariableSet.create()
                    .add("status",teamChat)
                    .add("statusFormatted", teamChat ? Messages.STAFF_STATUS_LOGIN :  Messages.STAFF_STATUS_LOGOUT));
        }
    }

}
