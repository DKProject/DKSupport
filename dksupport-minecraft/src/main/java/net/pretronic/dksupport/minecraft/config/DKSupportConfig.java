package net.pretronic.dksupport.minecraft.config;

import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.document.annotations.DocumentKey;

import java.util.Arrays;
import java.util.Collection;

public class DKSupportConfig {

    @DocumentKey("player.onJoin.loginInfo")
    public static boolean PLAYER_ON_LOGIN_INFO = true;

    public static CommandConfiguration COMMAND_TICKET = CommandConfiguration.newBuilder()
            .name("ticket")
            .aliases("support")
            .permission("dksupport.command.ticket")
            .create();

    public static String PERMISSION_STAFF = "dksupport.staff";

    public static Collection<TicketTopic> TICKET_TOPICS = Arrays.asList(
            new TicketTopic("1", "Bug report", "pencil", "bug", "bugs"),
            new TicketTopic("2", "Question", "question"),
            new TicketTopic("3", "Ask for help", "bookmark_tabs"));

    public static boolean DKCONNECT_INTEGRATION_ENABLED = true;
    public static String DKCONNECT_INTEGRATION_VOICEADAPTER = "1234";
    public static String DKCONNECT_INTEGRATION_CATEGORY = "1234";
    public static String DKCONNECT_INTEGRATION_CHANNEL_NAME = "{playerName}";
    public static String DKCONNECT_INTEGRATION_EMBED_KEY = "dkconnect.voiceadapter.discord.syncChat";
    public static String DKCONNECT_INTEGRATION_TICKET_CREATE_CHANNEL_ID = "1234";
}
