/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Philipp Elvin Friedhoff
 * @since 21.06.20, 18:52
 * @web %web%
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package net.pretronic.dksupport.minecraft.commands.ticket;

import net.pretronic.dksupport.minecraft.PlayerSettingsKey;
import net.pretronic.dksupport.minecraft.commands.CommandUtil;
import net.pretronic.dksupport.minecraft.config.DKSupportConfig;
import net.pretronic.dksupport.minecraft.config.Messages;
import net.pretronic.libraries.command.command.BasicCommand;
import net.pretronic.libraries.command.command.configuration.CommandConfiguration;
import net.pretronic.libraries.command.sender.CommandSender;
import net.pretronic.libraries.utility.interfaces.ObjectOwner;
import org.mcnative.runtime.api.player.OnlineMinecraftPlayer;

public class TicketLoginCommand extends BasicCommand {

    public TicketLoginCommand(ObjectOwner owner) {
        super(owner, CommandConfiguration.newBuilder().name("login").permission(DKSupportConfig.PERMISSION_STAFF).create());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(CommandUtil.isConsole(sender)) return;
        OnlineMinecraftPlayer player = (OnlineMinecraftPlayer) sender;

        boolean current = player.hasSetting("DKSupport", PlayerSettingsKey.SUPPORT,true);
        CommandUtil.changeLogin(PlayerSettingsKey.SUPPORT,player,current,true);
    }
}
