package net.pretronic.dksupport.minecraft.config;

import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.document.annotations.DocumentKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class DKSupportConfig {

    @DocumentKey("player.onJoin.loginInfo")
    public static boolean PLAYER_ON_LOGIN_INFO = true;

    public static CommandConfiguration COMMAND_TICKET = CommandConfiguration.newBuilder()
            .name("ticket")
            .aliases("support")
            .permission("dksupport.command.ticket")
            .create();

    public static String PERMISSION_STAFF = "dksupport.staff";

    public static Collection<TicketTopic> TICKET_TOPICS = Arrays.asList(new TicketTopic("bugreport", "Bug report", "bug", "bugs"),
            new TicketTopic("question", "Question"),
            new TicketTopic("help", "Ask for help"));
}
