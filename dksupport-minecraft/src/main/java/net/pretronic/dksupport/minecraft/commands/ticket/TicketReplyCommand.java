package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.UUID;

public class TicketReplyCommand extends BasicCommand {

    private final DKSupport dkSupport;

    public TicketReplyCommand(ObjectOwner owner, DKSupport dkSupport) {
        super(owner, CommandConfiguration.name("reply"));
        this.dkSupport = dkSupport;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(CommandUtil.isConsole(sender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;

        Ticket ticket;
        String message;
        if(player.hasPermission(Permissions.STAFF)) {
            if(args.length == 0) {
                CommandUtil.sendTicketHelpMessage(sender);
                return;
            }
            UUID ticketId;
            try {
                ticketId = UUID.fromString(args[0]);
            } catch (IllegalArgumentException ignored) {
                CommandUtil.sendTicketHelpMessage(sender);
                return;
            }
            ticket = dkSupport.getTicketManager().getTicket(ticketId);
            if(ticket == null) {
                player.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
                return;
            }
            message = CommandUtil.readStringFromArguments(args, 1);
        } else {
            ticket = CommandUtil.getSelectedTicket(dkSupport, player);
            if(ticket == null) return;
            message = CommandUtil.readStringFromArguments(args, 0);
        }
        ticket.sendMessage(ticket.getParticipant(player.getUniqueId()), message);
    }
}
