package net.pretronic.dksupport.minecraft;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.common.DefaultDKSupport;
import net.pretronic.dksupport.minecraft.commands.ticket.TicketCommand;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.listeners.PerformListener;
import net.pretronic.dksupport.minecraft.listeners.PlayerListener;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKSupportPlugin extends MinecraftPlugin {

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKSupport is starting, please wait..");

        //@Todo license

        DKSupport instance = new DefaultDKSupport(getRuntime().getLocal().getEventBus(), getDatabaseOrCreate());

        getRuntime().getRegistry().registerService(this, DKSupport.class, instance);

        getConfiguration().load(DKSupportConfig.class);

        getRuntime().getLocal().getEventBus().subscribe(this,new PlayerListener(instance));
        getRuntime().getLocal().getEventBus().subscribe(this, new PerformListener());
        getRuntime().getLocal().getCommandManager().registerCommand(new TicketCommand(this,DKSupportConfig.COMMAND_TICKET, instance));
        getRuntime().getPlayerManager().registerPlayerAdapter(DKSupportPlayer.class, player -> instance.getPlayerManager().getPlayer(player.getUniqueId()));

        DescriberRegistrar.register(instance);

        getLogger().info("DKSupport started successfully");
    }

}
