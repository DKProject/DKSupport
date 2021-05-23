package net.pretronic.dksupport.minecraft.config;

import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.document.annotations.DocumentKey;

public class DKSupportConfig {

    @DocumentKey("player.onJoin.loginInfo")
    public static boolean PLAYER_ON_LOGIN_INFO = true;

    public static CommandConfiguration COMMAND_TICKET = CommandConfiguration.newBuilder()
            .name("ticket")
            .aliases("support")
            .permission("dksupport.command.ticket")
            .create();

    public static String PERMISSION_STAFF = "dksupport.staff";

}
