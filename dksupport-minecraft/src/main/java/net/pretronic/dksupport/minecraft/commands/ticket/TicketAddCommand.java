package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketParticipant;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Convert;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class TicketAddCommand extends BasicCommand {

    private final DKSupport dksupport;

    public TicketAddCommand(ObjectOwner owner, DKSupport dksupport) {
        super(owner, CommandConfiguration.newBuilder().name("add").permission(DKSupportConfig.PERMISSION_STAFF).create());
        this.dksupport = dksupport;
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer)sender;

        Ticket ticket = CommandUtil.getSelectedTicket(dksupport, player);
        if(ticket == null) {
            return;
        }

        String playerName = arguments[0];
        MinecraftPlayer target = McNative.getInstance().getPlayerManager().getPlayer(playerName);
        if(target == null) {
            sender.sendMessage(Messages.ERROR_PLAYER_NOT_FOUND, VariableSet.create().add("name", playerName));
            return;
        }
        DKSupportPlayer targetSupportPlayer = target.getAs(DKSupportPlayer.class);


        TicketParticipant participant = ticket.getParticipant(targetSupportPlayer);
        if(participant != null){
            sender.sendMessage(Messages.ERROR_PARTICIPANT_ALREADY, VariableSet.create()
                    .addDescribed("participant", participant));
            return;
        }

        participant = ticket.addParticipant(targetSupportPlayer);
        if(participant != null){
            sender.sendMessage(Messages.COMMAND_TICKET_ADD, VariableSet.create()
                    .addDescribed("participant", participant));
        }
    }
}
