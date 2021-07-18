package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketMessage;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class TicketHistoryCommand extends BasicCommand {

    private final DKSupport dkSupport;

    public TicketHistoryCommand(ObjectOwner owner, DKSupport dkSupport) {
        super(owner, CommandConfiguration.name("history"));
        this.dkSupport = dkSupport;
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if(CommandUtil.isConsole(sender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;

        Ticket ticket = CommandUtil.getSelectedTicket(dkSupport, player);
        if(ticket == null) return;
        for (TicketMessage message : ticket.getMessages()) {
            player.sendMessage(Messages.TICKET_MESSAGE_RECEIVED, VariableSet.create().addDescribed("message", message));
        }
    }
}
