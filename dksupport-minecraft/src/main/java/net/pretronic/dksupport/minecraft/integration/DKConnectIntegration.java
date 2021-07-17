package net.pretronic.dksupport.minecraft.integration;

import net.pretronic.dkconnect.api.DKConnect;
import net.pretronic.dkconnect.api.player.DKConnectPlayer;
import net.pretronic.dkconnect.api.player.Verification;
import net.pretronic.dkconnect.api.voiceadapter.VoiceAdapter;
import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.participant.TicketParticipantMessageEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.libraries.event.Listener;
import net.pretronic.libraries.utility.Validate;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class DKConnectIntegration {

    private final DKConnect dkConnect;

    public DKConnectIntegration(DKConnect dkConnect) {
        Validate.notNull(dkConnect);
        this.dkConnect = dkConnect;
    }

    @Listener
    public void onTicketCreated(TicketCreatedEvent event) {
        VoiceAdapter voiceAdapter = this.dkConnect.getVoiceAdapter(DKSupportConfig.DKCONNECT_INTEGRATION_VOICE_ADAPTER);
        if(voiceAdapter == null) throw new IllegalArgumentException("Can't find voice adapter " + DKSupportConfig.DKCONNECT_INTEGRATION_VOICE_ADAPTER);

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


        });
    }

    @Listener
    public void onTicketSend(TicketParticipantMessageEvent event) {

    }
}