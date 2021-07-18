package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class TicketUnselectCommand extends BasicCommand {

    private final DKSupport dkSupport;

    public TicketUnselectCommand(ObjectOwner owner, DKSupport dkSupport) {
        super(owner, CommandConfiguration.name("unselect"));
        this.dkSupport = dkSupport;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if(CommandUtil.isConsole(commandSender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) commandSender;
        Ticket ticket = CommandUtil.getSelectedTicket(dkSupport, player);
        if(ticket == null) return;
        CommandUtil.unselectTicket((MinecraftPlayer) commandSender);
        commandSender.sendMessage(Messages.COMMAND_TICKET_UNSELECT, VariableSet.create().addDescribed("ticket", ticket));
    }
}
