package net.pretronic.dksupport.minecraft.config;

import org.mcnative.runtime.api.text.Text;
import org.mcnative.runtime.api.text.components.MessageComponent;

public class Messages {

    public static MessageComponent<?> ERROR_ONLY_PLAYER = Text.ofMessageKey("dksupport.error.onlyPlayer");
    public static MessageComponent<?> ERROR_TICKET_NOTFOUND = Text.ofMessageKey("dksupport.error.ticket.notFound");
    public static final MessageComponent<?> ERROR_UUID_NOT_VALID = Text.ofMessageKey("dksupport.error.uuid.notValid");
    public static final MessageComponent<?> ERROR_PARTICIPANT_ALREADY = Text.ofMessageKey("dksupport.error.participant.already");
    public static final MessageComponent<?> ERROR_PARTICIPANT_NOT = Text.ofMessageKey("dksupport.error.participant.not");
    public static final MessageComponent<?> ERROR_ALREADY_OPEN_TICKET = Text.ofMessageKey("dksupport.error.alreadyOpenTicket");
    public static final MessageComponent<?> ERROR_TICKET_NOT_OPEN = Text.ofMessageKey("dksupport.error.ticket.notOpen");
    public static final MessageComponent<?> ERROR_TICKET_NOT_PROCESSING = Text.ofMessageKey("dksupport.error.ticket.notProcessing");
    public static final MessageComponent<?> ERROR_TICKET_NOT_SELECTED = Text.ofMessageKey("dksupport.error.ticket.notSelected");
    public static final MessageComponent<?> ERROR_PLAYER_NOT_FOUND = Text.ofMessageKey("dksupport.error.player.notFound");


    public static MessageComponent<?> STAFF_STATUS_NOW = Text.ofMessageKey("dksupport.player.staff.status.now");
    public static MessageComponent<?> STAFF_STATUS_ALREADY = Text.ofMessageKey("dksupport.player.staff.status.already");
    public static MessageComponent<?> STAFF_STATUS_CHANGE = Text.ofMessageKey("dksupport.player.staff.status.change");
    public static MessageComponent<?> STAFF_STATUS_NOT = Text.ofMessageKey("dksupport.player.staff.status.not");
    public static MessageComponent<?> STAFF_STATUS_LOGIN = Text.ofMessageKey("dksupport.player.staff.status.login");
    public static MessageComponent<?> STAFF_STATUS_LOGOUT = Text.ofMessageKey("dksupport.player.staff.status.logout");

    public static final MessageComponent<?> TICKET_CREATE_STAFF = Text.ofMessageKey("dksupport.ticket.create.staff");
    public static final MessageComponent<?> TICKET_MESSAGE_RECEIVED = Text.ofMessageKey("dksupport.ticket.message.received");

    public static final MessageComponent<?> COMMAND_TICKET_HELP_USER = Text.ofMessageKey("dksupport.command.ticket.help.user");
    public static final MessageComponent<?> COMMAND_TICKET_HELP_STAFF = Text.ofMessageKey("dksupport.command.ticket.help.staff");

    public static final MessageComponent<?> COMMAND_TICKET_CREATE = Text.ofMessageKey("dksupport.command.ticket.create");
    public static final MessageComponent<?> COMMAND_TICKET_LIST = Text.ofMessageKey("dksupport.command.ticket.list");
    public static final MessageComponent<?> COMMAND_TICKET_TAKE = Text.ofMessageKey("dksupport.command.ticket.take");
    public static final MessageComponent<?> COMMAND_TICKET_SELECT = Text.ofMessageKey("dksupport.command.ticket.select");
    public static final MessageComponent<?> COMMAND_TICKET_MY = Text.ofMessageKey("dksupport.command.ticket.my");
    public static final MessageComponent<?> COMMAND_TICKET_CLOSE = Text.ofMessageKey("dksupport.command.ticket.close");
    public static final MessageComponent<?> COMMAND_TICKET_LEAVE = Text.ofMessageKey("dksupport.command.ticket.leave");
    public static final MessageComponent<?> COMMAND_TICKET_ADD = Text.ofMessageKey("dksupport.command.ticket.add");
    public static final MessageComponent<?> COMMAND_TICKET_REMOVE = Text.ofMessageKey("dksupport.command.ticket.remove");
    public static final MessageComponent<?> COMMAND_TICKET_INFO = Text.ofMessageKey("dksupport.command.ticket.info");
}
