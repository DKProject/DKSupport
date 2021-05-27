package net.pretronic.dksupport.common;

import net.pretronic.databasequery.api.Database;
import net.pretronic.databasequery.api.collection.DatabaseCollection;
import net.pretronic.databasequery.api.collection.field.FieldOption;
import net.pretronic.databasequery.api.datatype.DataType;
import net.pretronic.databasequery.api.query.ForeignKey;

public class DKSupportStorage {

    private final Database database;

    private final DatabaseCollection tickets;
    private final DatabaseCollection ticketParticipants;
    private final DatabaseCollection ticketMessages;

    public DKSupportStorage(Database database) {
        this.database = database;

        this.tickets = database.createCollection("dksupport_tickets")
                .field("Id", DataType.UUID, FieldOption.PRIMARY_KEY)
                .field("Category", DataType.STRING)
                .field("State", DataType.STRING, FieldOption.NOT_NULL)
                .field("CreatorId", DataType.UUID, FieldOption.NOT_NULL)
                .field("Created", DataType.LONG, FieldOption.NOT_NULL)
                .create();

        this.ticketParticipants = database.createCollection("dksupport_ticket_participants")
                .field("TicketId", DataType.UUID, ForeignKey.of(this.tickets, "Id", ForeignKey.Option.CASCADE), FieldOption.NOT_NULL)
                .field("PlayerId", DataType.UUID, FieldOption.NOT_NULL)
                .field("Hidden", DataType.BOOLEAN, FieldOption.NOT_NULL)
                .field("Joined", DataType.LONG, FieldOption.NOT_NULL)
                .field("ReceiveMessages", DataType.BOOLEAN, FieldOption.NOT_NULL)
                .create();

        this.ticketMessages = database.createCollection("dksupport_ticket_messages")
                .field("TicketId", DataType.UUID, ForeignKey.of(this.tickets, "Id", ForeignKey.Option.CASCADE), FieldOption.NOT_NULL)
                .field("SenderId", DataType.UUID, FieldOption.NOT_NULL)
                .field("Message", DataType.STRING, FieldOption.NOT_NULL)
                .field("Time", DataType.LONG, FieldOption.NOT_NULL)
                .create();
    }

    public DatabaseCollection getTickets() {
        return tickets;
    }

    public DatabaseCollection getTicketParticipants() {
        return ticketParticipants;
    }

    public DatabaseCollection getTicketMessages() {
        return ticketMessages;
    }
}
