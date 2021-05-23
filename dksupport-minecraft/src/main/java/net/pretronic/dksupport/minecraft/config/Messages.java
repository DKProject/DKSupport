package net.pretronic.dksupport.minecraft.config;

import org.mcnative.runtime.api.text.Text;
import org.mcnative.runtime.api.text.components.MessageComponent;

public class Messages {

    public static MessageComponent<?> ERROR_ONLY_PLAYER = Text.ofMessageKey("dksupport.error.onlyPlayer");
    public static MessageComponent<?> ERROR_TICKET_NOTFOUND = Text.ofMessageKey("dksupport.ticket.notFound");


    public static MessageComponent<?> STAFF_STATUS_NOW = Text.ofMessageKey("dksupport.player.staff.status.now");
    public static MessageComponent<?> STAFF_STATUS_ALREADY = Text.ofMessageKey("dksupport.player.staff.status.already");
    public static MessageComponent<?> STAFF_STATUS_CHANGE = Text.ofMessageKey("dksupport.player.staff.status.change");
    public static MessageComponent<?> STAFF_STATUS_NOT = Text.ofMessageKey("dksupport.player.staff.status.not");
    public static MessageComponent<?> STAFF_STATUS_LOGIN = Text.ofMessageKey("dksupport.player.staff.status.login");
    public static MessageComponent<?> STAFF_STATUS_LOGOUT = Text.ofMessageKey("dksupport.player.staff.status.logout");


}
