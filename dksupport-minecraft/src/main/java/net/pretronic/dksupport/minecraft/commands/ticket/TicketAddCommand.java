package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

import java.util.UUID;

public class TicketAddCommand extends BasicCommand {

    private DKSupport dksupport;

    public TicketAddCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder().name("add").permission(DKSupportConfig.PERMISSION_STAFF).create());
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if(CommandUtil.isConsole(sender)) return;

        Ticket ticket = dksupport.getTicketManager().getTicket(UUID.fromString(arguments[0]));

        if(ticket == null){
            sender.sendMessage(Messages.ERROR_TICKET_NOTFOUND);
            return;
        }

        DKSupportPlayer player = ((OnlineMinecraftPlayer) sender).getAs(DKSupportPlayer.class);

        if(ticket.isParticipant(player)){
            //@Todo message
            return;
        }

        if(ticket.addParticipant(player) != null){
            //@Todo message
            return;
        }
    }
}
