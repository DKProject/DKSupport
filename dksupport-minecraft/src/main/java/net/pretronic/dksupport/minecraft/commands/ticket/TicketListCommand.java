package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

public class TicketListCommand extends BasicCommand {

    private final DKSupport dkSupport;

    public TicketListCommand(ObjectOwner owner, DKSupport dkSupport) {
        super(owner, CommandConfiguration.newBuilder().name("list").permission(DKSupportConfig.PERMISSION_STAFF).create());
        this.dkSupport = dkSupport;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Messages.COMMAND_TICKET_LIST, VariableSet.create()
                .addDescribed("tickets", this.dkSupport.getTicketManager().getTickets(TicketState.OPEN)));
    }
}
