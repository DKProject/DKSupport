package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
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

public class TicketTakeCommand extends BasicCommand {

    private final DKSupport dksupport;

    public TicketTakeCommand(ObjectOwner owner, DKSupport dksupport) {
        super(owner, CommandConfiguration.newBuilder().name("take").permission(DKSupportConfig.PERMISSION_STAFF).create());
        this.dksupport = dksupport;
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;
        Ticket ticket = dksupport.getTicketManager().getTicket(UUID.fromString(arguments[0]));

        if(ticket == null){
            sender.sendMessage(Messages.ERROR_TICKET_NOTFOUND);
            return;
        }

        if(ticket.getState() != TicketState.OPEN){
            sender.sendMessage(Messages.ERROR_TICKET_NOT_OPEN);
            return;
        }
        OnlineMinecraftPlayer player = ((OnlineMinecraftPlayer) sender);
        if(ticket.take(player.getAs(DKSupportPlayer.class)) != null){
            CommandUtil.setSelectedTicket(player, ticket.getId());
            sender.sendMessage(Messages.COMMAND_TICKET_TAKE, VariableSet.create().addDescribed("ticket", ticket));
        }
    }
}
