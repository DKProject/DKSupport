package net.pretronic.dksupport.common.ticket;

import net.pretronic.databasequery.api.query.result.QueryResultEntry;
import net.pretronic.dksupport.api.event.ticket.TicketCreateEvent;
import net.pretronic.dksupport.api.event.ticket.TicketDeleteEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketManager;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.common.DefaultDKSupport;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketCreateEvent;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketDeleteEvent;
import net.pretronic.libraries.caching.ArrayCache;
import net.pretronic.libraries.caching.Cache;
import net.pretronic.libraries.caching.CacheQuery;
import net.pretronic.libraries.utility.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DefaultTicketManager implements TicketManager {

    private final DefaultDKSupport dkSupport;

    private final Cache<Ticket> ticketCache;

    public DefaultTicketManager(DefaultDKSupport dkSupport) {
        this.dkSupport = dkSupport;
        this.ticketCache = new ArrayCache<>();
        ticketCache.setExpireAfterAccess(30, TimeUnit.MINUTES)
                .setMaxSize(1000)
                .registerQuery("byId", new ByIdQuery())
                .registerQuery("byCreatorId", new ByCreatorIdAndStateQuery());
    }

    @Override
    public Ticket getTicket(UUID id) {
        return this.ticketCache.get("byId", id);
    }

    @Override
    public @NotNull Collection<Ticket> getTickets(@NotNull TicketState state) {
        Collection<Ticket> tickets = new ArrayList<>();
        this.dkSupport.getStorage().getTickets().find()
                .get("Id")
                .where("State", state)
                .execute().loadIn(tickets, resultEntry -> getTicket(resultEntry.getUniqueId("Id")));
        return tickets;
    }

    @Override
    public Ticket getOpenTicketForCreator(DKSupportPlayer player) {
        return this.ticketCache.get("byCreatorId", player.getId(), TicketState.OPEN);
    }

    @Override
    public Ticket createTicket(@NotNull DKSupportPlayer creator) {
        Ticket ticket = new DefaultTicket(this.dkSupport, creator.getId());

        TicketCreateEvent event = new DefaultTicketCreateEvent(ticket, creator);
        this.dkSupport.getEventBus().callEvent(TicketCreateEvent.class, event);
        if(event.isCancelled()) return null;

        this.dkSupport.getStorage().getTickets().insert()
                .set("Id", ticket.getId())
                .set("State", ticket.getState())
                .set("Created", ticket.getCreated())
                .set("CreatorId", creator.getId())
                .execute();

        this.ticketCache.insert(ticket);
        ticket.addParticipant(creator);
        return ticket;
    }

    @Override
    public boolean deleteTicket(@NotNull Ticket ticket) {
        TicketDeleteEvent event = new DefaultTicketDeleteEvent(ticket);
        this.dkSupport.getEventBus().callEvent(TicketDeleteEvent.class, event);
        if(event.isCancelled()) return false;

        this.dkSupport.getStorage().getTickets().delete()
                .where("Id", ticket.getId())
                .execute();
        this.ticketCache.remove(ticket);
        return true;
    }

    private class ByIdQuery implements CacheQuery<Ticket> {

        @Override
        public boolean check(Ticket ticket, Object[] identifiers) {
            UUID id = (UUID) identifiers[0];
            return ticket.getId().equals(id);
        }

        @Override
        public void validate(Object[] identifiers) {
            Validate.isTrue(identifiers.length == 1 && identifiers[0] instanceof UUID);
        }

        @Override
        public Ticket load(Object[] identifiers) {
            UUID id = (UUID) identifiers[0];
            QueryResultEntry resultEntry = dkSupport.getStorage().getTickets().find()
                    .where("Id", id)
                    .execute().firstOrNull();
            if(resultEntry == null) return null;
            return new DefaultTicket(dkSupport,
                    resultEntry.getUniqueId("Id"),
                    resultEntry.getString("Category"),
                    TicketState.parse(resultEntry.getString("State")),
                    resultEntry.getLong("Created"),
                    resultEntry.getUniqueId("CreatorId"));
        }
    }

    private class ByCreatorIdAndStateQuery implements CacheQuery<Ticket> {

        @Override
        public boolean check(Ticket ticket, Object[] identifiers) {
            UUID id = (UUID) identifiers[0];
            TicketState state = (TicketState) identifiers[1];
            return ticket.getCreator().getPlayer().getId().equals(id) && ticket.getState() == state;
        }

        @Override
        public void validate(Object[] identifiers) {
            Validate.isTrue(identifiers.length == 2 && identifiers[0] instanceof UUID && identifiers[1] instanceof TicketState);
        }

        @Override
        public Ticket load(Object[] identifiers) {
            UUID creatorId = (UUID) identifiers[0];
            TicketState state = (TicketState) identifiers[1];

            QueryResultEntry resultEntry = dkSupport.getStorage().getTickets().find()
                    .where("CreatorId", creatorId)
                    .where("State", state)
                    .execute().firstOrNull();
            if(resultEntry == null) return null;
            return new DefaultTicket(dkSupport,
                    resultEntry.getUniqueId("Id"),
                    resultEntry.getString("Category"),
                    TicketState.parse(resultEntry.getString("State")),
                    resultEntry.getLong("Created"),
                    creatorId);
        }
    }
}
