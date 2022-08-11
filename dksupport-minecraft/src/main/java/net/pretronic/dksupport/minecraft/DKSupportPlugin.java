package net.pretronic.dksupport.minecraft;

import net.pretronic.dksupport.api.DKSupport;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.common.DefaultDKSupport;
import net.pretronic.dksupport.minecraft.commands.DKSupportCommand;
import net.pretronic.dksupport.minecraft.commands.ticket.TicketCommand;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.integration.DKConnectIntegration;
import net.pretronic.dksupport.minecraft.listeners.PerformListener;
import net.pretronic.dksupport.minecraft.listeners.PlayerListener;
import net.pretronic.libraries.plugin.lifecycle.Lifecycle;
import net.pretronic.libraries.plugin.lifecycle.LifecycleState;
import org.mcnative.licensing.context.platform.McNativeLicenseIntegration;
import org.mcnative.licensing.exceptions.CloudNotCheckoutLicenseException;
import org.mcnative.licensing.exceptions.LicenseNotValidException;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

public class DKSupportPlugin extends MinecraftPlugin {

    private DKConnectIntegration dkConnectIntegration;

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKSupport is starting, please wait..");

        DKSupport instance = new DefaultDKSupport(getRuntime().getLocal().getEventBus(), getDatabaseOrCreate());

        getRuntime().getRegistry().registerService(this, DKSupport.class, instance);

        getConfiguration().load(DKSupportConfig.class);

        getRuntime().getLocal().getEventBus().subscribe(this,new PlayerListener(instance));
        getRuntime().getLocal().getEventBus().subscribe(this, new PerformListener(this, instance));
        getRuntime().getLocal().getCommandManager().registerCommand(new TicketCommand(this,DKSupportConfig.COMMAND_TICKET, instance));
        getRuntime().getLocal().getCommandManager().registerCommand(new DKSupportCommand(this));
        getRuntime().getPlayerManager().registerPlayerAdapter(DKSupportPlayer.class, player -> instance.getPlayerManager().getPlayer(player.getUniqueId()));

        DescriberRegistrar.register(instance);

        if(DKSupportConfig.DKCONNECT_INTEGRATION_ENABLED && this.dkConnectIntegration == null) {
            try {
                Class<?> dkconnectClass = Class.forName("net.pretronic.dkconnect.api.DKConnect");
                getLogger().info("Enabling DKConnect integration");
                this.dkConnectIntegration = new DKConnectIntegration(this, instance, (net.pretronic.dkconnect.api.DKConnect) McNative.getInstance().getRegistry().getService(dkconnectClass));
            } catch (ClassNotFoundException ignored) {}
        }

        getLogger().info("DKSupport started successfully");
    }

    public DKConnectIntegration getDKConnectIntegration() {
        return dkConnectIntegration;
    }
}
