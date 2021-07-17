package net.pretronic.dksupport.minecraft.integration;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.pretronic.dkconnect.api.DKConnect;
import net.pretronic.dkconnect.api.player.DKConnectPlayer;
import net.pretronic.dkconnect.api.player.Verification;
import net.pretronic.dkconnect.api.voiceadapter.VoiceAdapter;
import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.minecraft.DKSupportPlugin;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.message.StringTextable;
import net.pretronic.libraries.message.Textable;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Validate;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.MinecraftPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DKConnectIntegration {

    private final DKSupport dkSupport;
    private final DKConnect dkConnect;
    private final Map<UUID, String> ticketDiscordChannelMapping;

    public DKConnectIntegration(DKSupportPlugin plugin, DKSupport dkSupport, DKConnect dkConnect) {
        this.dkSupport = dkSupport;
        Validate.notNull(dkConnect);
        this.dkConnect = dkConnect;
        this.ticketDiscordChannelMapping = new ConcurrentHashMap<>();
        McNative.getInstance().getLocal().getEventBus().subscribe(plugin, this);
    }

    @Listener
    public void onTicketCreated(TicketCreatedEvent event) {
        VoiceAdapter voiceAdapter = DKSupportConfig.getDKConnectIntegrationVoiceAdapter(dkConnect);

        Ticket ticket = event.getTicket();
        MinecraftPlayer creator = McNative.getInstance().getPlayerManager().getPlayer(event.getTicket().getCreator().getPlayer().getId());
        DKConnectPlayer creatorConnectPlayer = creator.getAs(DKConnectPlayer.class);
        Verification verification = creatorConnectPlayer.getVerification(voiceAdapter);
        String[] allowedUserIds = null;
        if(verification != null) {
            allowedUserIds = new String[]{verification.getUserId()};
        }

        voiceAdapter.createTextChannel(DKSupportConfig.DKCONNECT_INTEGRATION_CATEGORY,
                DKSupportConfig.DKCONNECT_INTEGRATION_CHANNEL_NAME.replace("{ticketId}", ticket.getId().toString()).replace("{playerName}", creator.getName()),
                null,
                allowedUserIds).thenAccept(channelId -> {
                    this.ticketDiscordChannelMapping.put(ticket.getId(), channelId);
        });
    }

    @Listener
    public void onTicketSend(TicketParticipantMessageEvent event) {
        if(event.getSource().equalsIgnoreCase("discord")) return;
        TicketMessage ticketMessage = event.getMessage();

        VoiceAdapter voiceAdapter = DKSupportConfig.getDKConnectIntegrationVoiceAdapter(dkConnect);
        String channelId = this.ticketDiscordChannelMapping.get(event.getTicket().getId());
        if(channelId == null) throw new IllegalArgumentException("Can't retrieve discord channel id for ticket " + event.getTicket().getId());

        Textable text = voiceAdapter.getMessage("dkconnect.voiceadapter.discord.syncChat");
        voiceAdapter.sendMessage(channelId, text, VariableSet.create()
                .addDescribed("event", event)
                .addDescribed("player", McNative.getInstance().getPlayerManager().getPlayer(ticketMessage.getSender().getId()).getAs(DKConnectPlayer.class))
                .add("message", event.getMessage()));
    }

    @Listener
    public void onDiscordMessage(GuildMessageReceivedEvent event) {
        for (Map.Entry<UUID, String> entry : this.ticketDiscordChannelMapping.entrySet()) {
            String channelId = entry.getValue();
            if(event.getChannel().getId().equals(channelId)) {
                VoiceAdapter voiceAdapter = DKSupportConfig.getDKConnectIntegrationVoiceAdapter(dkConnect);
                DKConnectPlayer player = dkConnect.getPlayerManager().getPlayerByVerificationUserId(voiceAdapter, event.getAuthor().getId());
                if(player == null) {
                    event.getMessage().delete().queue();
                    voiceAdapter.sendMessage("", null, voiceAdapter.getMessage("dkconnect.voiceadapter.discord.notVerified"), VariableSet.create()
                            .addDescribed("event", event)
                            .addDescribed("mention", event.getAuthor().getAsMention()));
                    return;
                }
                Ticket ticket = dkSupport.getTicketManager().getTicket(entry.getKey());
                if(ticket == null) throw new IllegalArgumentException("Can't find ticket " + entry.getKey());

                TicketParticipant participant = ticket.getParticipant(player.getId());
                if(participant == null) {
                    participant = ticket.addParticipant(dkSupport.getPlayerManager().getPlayer(player.getId()));
                }
                ticket.sendMessage("discord", participant, event.getMessage().getContentRaw());
            }
        }
    }
}
