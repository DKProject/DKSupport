package net.pretronic.dksupport.minecraft;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.common.player.DefaultDKSupportPlayer;
import net.pretronic.dksupport.common.ticket.DefaultTicket;
import net.pretronic.dksupport.common.ticket.DefaultTicketMessage;
import net.pretronic.dksupport.common.ticket.DefaultTicketParticipant;
import net.pretronic.libraries.message.bml.variable.describer.VariableDescriber;
import net.pretronic.libraries.message.bml.variable.describer.VariableDescriberRegistry;
import net.pretronic.libraries.utility.Convert;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;

import java.util.ArrayList;
import java.util.Collection;

public class DescriberRegistrar {

    public static void register(DKSupport dkSupport) {
        VariableDescriberRegistry.registerDescriber(DefaultTicket.class);
        VariableDescriberRegistry.registerDescriber(DefaultTicketMessage.class);

        VariableDescriber<DefaultDKSupportPlayer> playerDescriber = VariableDescriberRegistry.registerDescriber(DefaultDKSupportPlayer.class);
        playerDescriber.setForwardFunction(player -> McNative.getInstance().getPlayerManager().getPlayer(player.getId()));
        playerDescriber.registerFunction("selectedTicket", player -> {
            MinecraftPlayer player1 = McNative.getInstance().getPlayerManager().getPlayer(player.getId());
            Setting setting = player1.getSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
            if(setting == null) return null;
            return dkSupport.getTicketManager().getTicket(Convert.toUUID(setting.getObjectValue()));
        });
        //@Todo direct method in SupportPlayer?
        playerDescriber.registerFunction("myTickets", player -> {
            Collection<Ticket> tickets = new ArrayList<>();
            for (Ticket processingTicket : player.getTickets(TicketState.PROCESSING)) {
                if(!processingTicket.getCreator().getPlayer().getId().equals(player.getId())) {
                    tickets.add(processingTicket);
                }
            }
            return tickets;
        });

        VariableDescriber<DefaultTicketParticipant> participantDescriber = VariableDescriberRegistry.registerDescriber(DefaultTicketParticipant.class);
        participantDescriber.setForwardFunction(participant -> dkSupport.getPlayerManager().getPlayer(participant.getPlayer().getId()));
    }
}
