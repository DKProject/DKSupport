package net.pretronic.dksupport.minecraft.commands;

import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;
import org.mcnative.runtime.api.text.Text;
import org.mcnative.runtime.api.text.event.ClickAction;
import org.mcnative.runtime.api.text.format.TextColor;

public class DKSupportCommand extends BasicCommand {

    public DKSupportCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.name("dksupport"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Text.newBuilder()
                .color(TextColor.DARK_GRAY).text("» ")
                .color(TextColor.YELLOW).text("DKSupport")
                .color(TextColor.DARK_GRAY).text(" | ")
                .color(TextColor.GRAY).text("v")
                .color(TextColor.RED).text(((MinecraftPlugin)getOwner()).getDescription().getVersion().getName())
                .color(TextColor.GRAY).text(" by ")
                .color(TextColor.BLUE).text(((MinecraftPlugin)getOwner()).getDescription().getAuthor()).onClick(ClickAction.OPEN_URL, "https://pretronic.net/dkplugins/")
                .build());
    }
}
