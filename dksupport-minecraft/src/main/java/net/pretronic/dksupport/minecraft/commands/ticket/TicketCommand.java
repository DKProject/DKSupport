package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.NotFindable;
import net.pretronic.libraries.command.command.MainCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class TicketCommand extends MainCommand implements NotFindable {

    private final DKSupport dkSupport;

    public TicketCommand(ObjectOwner owner, CommandConfiguration configuration, DKSupport dkSupport) {
        super(owner, configuration);
        this.dkSupport = dkSupport;

        registerCommand(new TicketLoginCommand(owner));
        registerCommand(new TicketLogoutCommand(owner));
        registerCommand(new TicketToggleCommand(owner));
        registerCommand(new TicketTakeCommand(owner, dkSupport));
        registerCommand(new TicketCloseCommand(owner, dkSupport));
        registerCommand(new TicketLeaveCommand(owner, dkSupport));
        registerCommand(new TicketAddCommand(owner, dkSupport));
        registerCommand(new TicketRemoveCommand(owner, dkSupport));
        registerCommand(new TicketListCommand(owner, dkSupport));
        registerCommand(new TicketSelectCommand(owner, dkSupport));
        registerCommand(new TicketMyCommand(owner));
        registerCommand(new TicketInfoCommand(owner, dkSupport));
    }

    @Override
    public void commandNotFound(CommandSender sender, String command, String[] args) {
        if(CommandUtil.isConsole(sender)) return;
        if(command == null || command.trim().isEmpty()) {
            CommandUtil.sendTicketHelpMessage(sender);
            return;
        }
        DKSupportPlayer player = ((MinecraftPlayer)sender).getAs(DKSupportPlayer.class);
        if(dkSupport.getTicketManager().getOpenTicketForCreator(player) != null) {
            sender.sendMessage(Messages.ERROR_ALREADY_OPEN_TICKET);
            return;
        }
        String category = command + CommandUtil.readStringFromArguments(args, 0);
        Ticket ticket = this.dkSupport.getTicketManager().createTicket(player);
        if(ticket != null) {
            ticket.setCategory(category);
            sender.sendMessage(Messages.COMMAND_TICKET_CREATE, VariableSet.create()
                    .addDescribed("ticket", ticket));
        }
    }
}
