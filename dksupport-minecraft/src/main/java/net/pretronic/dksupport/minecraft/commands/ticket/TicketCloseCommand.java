package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.Convert;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.UUID;

public class TicketCloseCommand extends BasicCommand {

    private final DKSupport dksupport;

    public TicketCloseCommand(ObjectOwner owner, DKSupport dksupport) {
        super(owner, CommandConfiguration.newBuilder().name("close").create());
        this.dksupport = dksupport;
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;
        Ticket ticket;
        if(arguments.length == 0) {
            ticket = CommandUtil.getSelectedTicket(dksupport, player);
            if(ticket == null) return;
        } else {
            if(!sender.hasPermission(Permissions.STAFF)) {
                sender.sendMessage(Messages.ERROR_NOT_STAFF);
                return;
            }
            String rawTicketId = arguments[0];
            try {
                ticket = this.dksupport.getTicketManager().getTicket(Convert.toUUID(rawTicketId));
                if(ticket == null) {
                    sender.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
                    return;
                }
                if(ticket.getState() == TicketState.CLOSED) {
                    player.removeSetting("DKSupport", PlayerSettingsKey.TICKET_SELECTED);
                    sender.sendMessage(Messages.ERROR_TICKET_NOT_SELECTED);
                    return;
                }
            } catch (IllegalArgumentException exception) {
                sender.sendMessage(Messages.ERROR_UUID_NOT_VALID, VariableSet.create().addDescribed("value", rawTicketId));
                return;
            }
        }

        if(ticket.setState(TicketState.CLOSED)) {
            sender.sendMessage(Messages.COMMAND_TICKET_CLOSE);
        }
    }
}
