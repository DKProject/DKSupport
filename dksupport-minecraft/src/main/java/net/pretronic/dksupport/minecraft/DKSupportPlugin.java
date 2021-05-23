package net.pretronic.dksupport.minecraft;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.minecraft.commands.ticket.TicketCommand;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.listeners.PlayerListener;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKSupportPlugin extends MinecraftPlugin {

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKSupport is starting, please wait..");

        //@Todo license

        DKSupport instance = null;

        getConfiguration().load(DKSupportConfig.class);

        getRuntime().getLocal().getEventBus().subscribe(this,new PlayerListener());
        getRuntime().getLocal().getCommandManager().registerCommand(new TicketCommand(this,DKSupportConfig.COMMAND_TICKET));
        getRuntime().getPlayerManager().registerPlayerAdapter(DKSupportPlayer.class, player -> instance.getPlayerManager().getPlayer(player.getUniqueId()));

        getLogger().info("DKSupport started successfully");
    }

}
