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

    public static final String RESOURCE_ID = "a120140d-bbdf-11eb-8ba0-0242ac180002";
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcUY5PfVh6jLu7pP27cAtp95HwNpxfvE5rnv1ptBmYRUMIJS1vCutfjlEogfTMZL2aiKhdAKW4wuGNzzGPKxVcV/iVRiGtJPHRPpdvIgPkGVrglZVDrV8pcyhLd7L42erskdUZl9iAvXx3KZVp/5q/njRz0n9ZWGU7KHs4+ngpCRyFw1N1Kr/tZkqz+BgLXNT3Fxv2EU6qDpLNutsXkojmO+oMUsz8sFDPf38aFzn8lNNpFNTxdJgaaMEZN12G9WKj6XmKIdbq04Qcb7oX0BhugqPbvW0bULG+ija9aEOevdyFLrKrDT34qUmo8PmDPh1/FZmazsPTv1HIWkNhY1qwIDAQAB";

    private DKConnectIntegration dkConnectIntegration;

    @Lifecycle(state = LifecycleState.LOAD)
    public void onLoad(LifecycleState state){
        getLogger().info("DKSupport is starting, please wait..");

        try{
            McNativeLicenseIntegration.newContext(this,RESOURCE_ID,PUBLIC_KEY).verifyOrCheckout();
        }catch (LicenseNotValidException | CloudNotCheckoutLicenseException e){
            getLogger().error("--------------------------------");
            getLogger().error("-> Invalid license");
            getLogger().error("-> Error: "+e.getMessage());
            getLogger().error("--------------------------------");
            getLogger().info("DKSupport is shutting down");
            getLoader().shutdown();
            return;
        }

        DKSupport instance = new DefaultDKSupport(getRuntime().getLocal().getEventBus(), getDatabaseOrCreate());

        getRuntime().getRegistry().registerService(this, DKSupport.class, instance);

        getConfiguration().load(DKSupportConfig.class);

        getRuntime().getLocal().getEventBus().subscribe(this,new PlayerListener(instance));
        getRuntime().getLocal().getEventBus().subscribe(this, new PerformListener(this));
        getRuntime().getLocal().getCommandManager().registerCommand(new TicketCommand(this,DKSupportConfig.COMMAND_TICKET, instance));
        getRuntime().getLocal().getCommandManager().registerCommand(new DKSupportCommand(this));
        getRuntime().getPlayerManager().registerPlayerAdapter(DKSupportPlayer.class, player -> instance.getPlayerManager().getPlayer(player.getUniqueId()));

        DescriberRegistrar.register(instance);

        if(DKSupportConfig.DKCONNECT_INTEGRATION_ENABLED && this.dkConnectIntegration == null) {
            try {
                Class<?> dkconnectClass = Class.forName("net.pretronic.dkconnect.api.DKConnect");
                getLogger().info("Enabling DKConnect integration");
                this.dkConnectIntegration = new DKConnectIntegration((net.pretronic.dkconnect.api.DKConnect) McNative.getInstance().getRegistry().getService(dkconnectClass));
            } catch (ClassNotFoundException ignored) {}
        }

        getLogger().info("DKSupport started successfully");
    }

    public DKConnectIntegration getDKConnectIntegration() {
        return dkConnectIntegration;
    }
}
