package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.UUID;

public class TicketLeaveCommand extends BasicCommand {

    private final DKSupport dksupport;

    public TicketLeaveCommand(ObjectOwner owner, DKSupport dksupport) {
        super(owner, CommandConfiguration.newBuilder().name("leave").permission(DKSupportConfig.PERMISSION_STAFF).create());
        this.dksupport = dksupport;
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;

        Ticket ticket;
        if(arguments.length == 1) {
            ticket = dksupport.getTicketManager().getTicket(UUID.fromString(arguments[0]));
            if(ticket == null){
                sender.sendMessage(Messages.ERROR_TICKET_NOTFOUND, VariableSet.create().add("id", arguments[0]));
                return;
            }
        } else {
            ticket = CommandUtil.getSelectedTicket(dksupport, (OnlineMinecraftPlayer) sender);
            if(ticket == null) return;
        }

        DKSupportPlayer player = ((OnlineMinecraftPlayer) sender).getAs(DKSupportPlayer.class);

        if(!ticket.isParticipant(player)){
            sender.sendMessage(Messages.ERROR_PARTICIPANT_NOT, VariableSet.create().addDescribed("player", player));
            return;
        }

        if(ticket.removeParticipant(player) != null){
            sender.sendMessage(Messages.COMMAND_TICKET_LEAVE, VariableSet.create()
                    .addDescribed("ticket", ticket));
            CommandUtil.unselectTicket((MinecraftPlayer) sender);
            return;
        }
    }
}
