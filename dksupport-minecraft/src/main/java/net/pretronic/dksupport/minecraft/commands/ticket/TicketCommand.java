package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.libraries.command.command.MainCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;

public class TicketCommand extends MainCommand {

    public TicketCommand(ObjectOwner owner, CommandConfiguration configuration) {
        super(owner, configuration);

        registerCommand(new TicketLoginCommand(owner));
        registerCommand(new TicketLoginCommand(owner));
        registerCommand(new TicketToggleCommand(owner));
        registerCommand(new TicketTakeCommand(owner));
        registerCommand(new TicketCloseCommand(owner));
        registerCommand(new TicketLeaveCommand(owner));

    }
}
