package net.pretronic.dksupport.minecraft.integration;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.pretronic.dkconnect.api.DKConnect;
import net.pretronic.dkconnect.api.player.DKConnectPlayer;
import net.pretronic.dkconnect.api.player.Verification;
import net.pretronic.dkconnect.api.voiceadapter.VoiceAdapter;
import net.pretronic.dkconnect.api.voiceadapter.channel.TextChannel;
import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.TicketUpdateStateEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.DKSupportPlugin;
import net.pretronic.dksupport.minecraft.PluginSettingsKey;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.message.Textable;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.StringUtil;
import net.pretronic.libraries.utility.Validate;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.MinecraftPlayer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DKConnectIntegration {

    private final DKSupportPlugin plugin;
    private final DKSupport dkSupport;
    private final DKConnect dkConnect;
    private final Map<UUID, String> ticketDiscordChannelMapping;

    public DKConnectIntegration(DKSupportPlugin plugin, DKSupport dkSupport, DKConnect dkConnect) {
        Validate.notNull(plugin, dkSupport, dkConnect);
        this.plugin = plugin;
        this.dkSupport = dkSupport;
        this.dkConnect = dkConnect;
        this.ticketDiscordChannelMapping = new ConcurrentHashMap<>();

        McNative.getInstance().getLocal().getEventBus().subscribe(plugin, this);

        initMessages();
    }

    private void initMessages() {
        importMessages();
        initTicketCreateMessage();
    }

    private void importMessages() {
        VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);
        Stream<Path> walk = getDirectoryFiles("/dkconnect-integration/messages");
        if(walk == null) return;
        for (Iterator<Path> iterator = walk.iterator(); iterator.hasNext();){
            Path child = iterator.next();
            if(Files.isRegularFile(child)) {
                voiceAdapter.importMessage(StringUtil.split(child.getFileName().toString(), '.')[0].replace("-", "."),
                        DKSupportPlugin.class.getResourceAsStream(child.toString()));
            }
        }
    }

    private void initTicketCreateMessage() {
        if(!plugin.hasSetting(PluginSettingsKey.TICKET_CREATE_MESSAGE_ID)) {
            String channelId = DKSupportConfig.DKCONNECT_INTEGRATION_TICKET_CREATE_CHANNEL_ID;
            try {
                VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);
                TextChannel channel = voiceAdapter.getTextChannel(channelId);
                channel.sendMessage(voiceAdapter.getMessage(DKConnectIntegrationMessages.TICKET_CREATE), VariableSet.create()).thenAccept(message -> {
                    plugin.getLogger().info("Successful sent ticket create message");
                });
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Can't create ticket create message for channelId " + channelId, exception);
            }
        }
    }

    public static VoiceAdapter getDKConnectIntegrationVoiceAdapter(DKConnect dkConnect) {
        VoiceAdapter voiceAdapter = dkConnect.getVoiceAdapter(DKSupportConfig.DKCONNECT_INTEGRATION_VOICEADAPTER);
        if(voiceAdapter == null) throw new IllegalArgumentException("Can't find voice adapter " + DKSupportConfig.DKCONNECT_INTEGRATION_VOICEADAPTER);
        return voiceAdapter;
    }

    @Listener
    public void onTicketCreated(TicketCreatedEvent event) {
        VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);

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
                allowedUserIds).thenAccept(channel -> {
                    this.ticketDiscordChannelMapping.put(ticket.getId(), channel.getId());
        });
    }

    @Listener
    public void onTicketSend(TicketParticipantMessageEvent event) {
        if(event.getSource().equalsIgnoreCase("discord")) return;
        McNative.getInstance().getScheduler().createTask(McNative.getInstance())
                .delay(1, TimeUnit.SECONDS)
                .execute(()-> {
                    TicketMessage ticketMessage = event.getMessage();

                    VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);
                    String channelId = this.ticketDiscordChannelMapping.get(event.getTicket().getId());
                    if(channelId == null) throw new IllegalArgumentException("Can't retrieve discord channel id for ticket " + event.getTicket().getId());

                    Textable text = voiceAdapter.getMessage(DKSupportConfig.DKCONNECT_INTEGRATION_EMBED_KEY);
                    TextChannel channel = voiceAdapter.getTextChannel(channelId);
                    channel.sendMessage(text, VariableSet.create()
                            .addDescribed("player", McNative.getInstance().getPlayerManager().getPlayer(ticketMessage.getSender().getId()).getAs(DKConnectPlayer.class))
                            .add("message", event.getMessage().getText()));
                });
    }

    @Listener
    public void onTicket(TicketUpdateStateEvent event) {
        if(event.getNewState() == TicketState.CLOSED) {
            for (Map.Entry<UUID, String> entry : this.ticketDiscordChannelMapping.entrySet()) {
                Ticket ticket = dkSupport.getTicketManager().getTicket(entry.getKey());
                if(ticket == null) throw new IllegalArgumentException("Can't find ticket " + entry.getKey());
                VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);
                voiceAdapter.getTextChannel(entry.getValue()).delete();
            }
        }
    }

    @Listener
    public void onDiscordMessage(GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        for (Map.Entry<UUID, String> entry : this.ticketDiscordChannelMapping.entrySet()) {
            String channelId = entry.getValue();
            if(event.getChannel().getId().equals(channelId)) {
                VoiceAdapter voiceAdapter = getDKConnectIntegrationVoiceAdapter(dkConnect);
                DKConnectPlayer player = dkConnect.getPlayerManager().getPlayerByVerificationUserId(voiceAdapter, event.getAuthor().getId());
                if(player == null) {
                    event.getMessage().delete().queue();
                    voiceAdapter.getTextChannel(channelId).sendMessage(voiceAdapter.getMessage("dkconnect.voiceadapter.discord.notVerified"), VariableSet.create()
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

    private Stream<Path> getDirectoryFiles(String folder) {
        URI uri = null;
        try {
            URL url = DKSupportPlugin.class.getResource(folder);
            if(url == null) return null;
            uri = url.toURI();
        } catch (URISyntaxException e) {
            return null;
        }

        Path path;
        if (uri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException("Can't get file system for folder " + folder, e);
            }
            path = fileSystem.getPath(folder);
        } else {
            path = Paths.get(uri);
        }
        Stream<Path> walk;
        try {
            walk = Files.walk(path, 1);
        } catch (IOException e) {
            throw new RuntimeException("Can't get stream for folder " + folder, e);
        }
        return walk;
    }
}
