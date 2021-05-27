package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.UUID;

public class TicketSelectCommand extends BasicCommand {

    private final DKSupport dkSupport;

    public TicketSelectCommand(ObjectOwner owner, DKSupport dkSupport) {
        super(owner, CommandConfiguration.newBuilder().name("select").permission(Permissions.STAFF).create());
        this.dkSupport = dkSupport;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(CommandUtil.isConsole(sender)) return;
        Ticket ticket = dkSupport.getTicketManager().getTicket(UUID.fromString(args[0]));

        if(ticket == null){
            sender.sendMessage(Messages.ERROR_TICKET_NOTFOUND);
            return;
        }
        if(ticket.getState() != TicketState.PROCESSING){
            sender.sendMessage(Messages.ERROR_TICKET_NOT_PROCESSING);
            return;
        }

        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;

        ticket.addParticipant(player.getAs(DKSupportPlayer.class));
        player.setSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED, ticket.getId());
        player.sendMessage(Messages.COMMAND_TICKET_SELECT, VariableSet.create().addDescribed("ticket", ticket));
    }
}
