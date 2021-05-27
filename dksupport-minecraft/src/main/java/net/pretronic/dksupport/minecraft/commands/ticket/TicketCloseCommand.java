package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
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
import net.pretronic.libraries.utility.Convert;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.Setting;
import org.mcnative.runtime.api.player.MinecraftPlayer;

import java.util.UUID;

public class TicketCloseCommand extends BasicCommand {

    private final DKSupport dksupport;

    public TicketCloseCommand(ObjectOwner owner, DKSupport dksupport) {
        super(owner, CommandConfiguration.newBuilder().name("close").permission(DKSupportConfig.PERMISSION_STAFF).create());
        this.dksupport = dksupport;
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;
        MinecraftPlayer player = (MinecraftPlayer) sender;
        UUID ticketId = null;
        if(arguments.length == 0) {
            Setting setting = player.getSetting("DKSupport", PlayerSettingsKey.SUPPORT);
            if(setting != null) {
                ticketId = Convert.toUUID(setting.getObjectValue());
            }
        } else {
            String rawTicketId = arguments[0];
            try {
                ticketId = Convert.toUUID(rawTicketId);
            } catch (IllegalArgumentException exception) {
                sender.sendMessage(Messages.ERROR_UUID_NOT_VALID, VariableSet.create().addDescribed("value", rawTicketId));
            }
        }
        Ticket ticket = this.dksupport.getTicketManager().getTicket(ticketId);

        if(ticket == null){
            sender.sendMessage(Messages.ERROR_TICKET_NOTFOUND);
            return;
        }

        if(ticket.setState(TicketState.CLOSED)) {
            sender.sendMessage(Messages.COMMAND_TICKET_CLOSE);
        }
    }
}
