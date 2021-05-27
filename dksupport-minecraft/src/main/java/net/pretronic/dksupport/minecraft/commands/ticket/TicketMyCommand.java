package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.dksupport.minecraft.config.Permissions;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.message.bml.variable.VariableSet;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.MinecraftPlayer;

public class TicketMyCommand extends BasicCommand {

    public TicketMyCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder().name("my").permission(Permissions.STAFF).create());
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if(CommandUtil.isConsole(sender)) return;
        DKSupportPlayer player = ((MinecraftPlayer)sender).getAs(DKSupportPlayer.class);
        sender.sendMessage(Messages.COMMAND_TICKET_MY, VariableSet.create().addDescribed("player", player));
    }


}
