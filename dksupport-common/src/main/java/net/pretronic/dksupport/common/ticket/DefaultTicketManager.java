package net.pretronic.dksupport.common.ticket;

import net.pretronic.databasequery.api.query.result.QueryResultEntry;
import net.pretronic.dksupport.api.event.ticket.TicketCreateEvent;
import net.pretronic.dksupport.api.event.ticket.TicketCreatedEvent;
import net.pretronic.dksupport.api.event.ticket.TicketDeleteEvent;
import net.pretronic.dksupport.api.player.DKSupportPlayer;
import net.pretronic.dksupport.api.ticket.Ticket;
import net.pretronic.dksupport.api.ticket.TicketManager;
import net.pretronic.dksupport.api.ticket.TicketState;
import net.pretronic.dksupport.common.DefaultDKSupport;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketCreateEvent;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketCreatedEvent;
import net.pretronic.dksupport.common.event.ticket.DefaultTicketDeleteEvent;
import net.pretronic.libraries.caching.ArrayCache;
import net.pretronic.libraries.caching.Cache;
import net.pretronic.libraries.caching.CacheQuery;
import net.pretronic.libraries.utility.Iterators;
import net.pretronic.libraries.utility.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
    public @NotNull Collection<Ticket> getTickets(@NotNull TicketState... states) {
        Collection<Ticket> tickets = new ArrayList<>();
        this.dkSupport.getStorage().getTickets().find()
                .get("Id")
                .whereIn("State", states)
                .execute().loadIn(tickets, resultEntry -> getTicket(resultEntry.getUniqueId("Id")));
        return tickets;
    }

    @Override
    public Ticket getTicketForCreator(DKSupportPlayer player, TicketState... states) {
        return this.ticketCache.get("byCreatorId", player.getId(), states);
    }

    @Override
    public @NotNull Collection<Ticket> getTickets(DKSupportPlayer player, TicketState state) {
        Collection<Ticket> tickets = new ArrayList<>();
        this.dkSupport.getStorage().getTickets().find()
                .get("Id")
                .join(this.dkSupport.getStorage().getTicketParticipants()).on("Id", "TicketId")
                .where("State", state)
                .where("PlayerId", player.getId())
                .execute().loadIn(tickets, resultEntry -> getTicket(resultEntry.getUniqueId("Id")));
        return tickets;
    }

    @Override
    public Ticket createTicket(@NotNull DKSupportPlayer creator, String topic) {
        Ticket ticket = new DefaultTicket(this.dkSupport, topic, creator.getId());

        TicketCreateEvent event = new DefaultTicketCreateEvent(creator, topic);
        this.dkSupport.getEventBus().callEvent(TicketCreateEvent.class, event);
        if(event.isCancelled()) return null;

        this.dkSupport.getStorage().getTickets().insert()
                .set("Id", ticket.getId())
                .set("State", ticket.getState())
                .set("Created", ticket.getCreated())
                .set("CreatorId", creator.getId())
                .set("Topic", topic)
                .execute();


        this.ticketCache.insert(ticket);
        ticket.addParticipant(creator);

        this.dkSupport.getEventBus().callEvent(TicketCreatedEvent.class, new DefaultTicketCreatedEvent(ticket));
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
                    resultEntry.getString("Topic"),
                    TicketState.parse(resultEntry.getString("State")),
                    resultEntry.getLong("Created"),
                    resultEntry.getUniqueId("CreatorId"));
        }
    }

    private class ByCreatorIdAndStateQuery implements CacheQuery<Ticket> {

        @Override
        public boolean check(Ticket ticket, Object[] identifiers) {
            UUID id = (UUID) identifiers[0];
            TicketState[] states = (TicketState[]) identifiers[1];
            return ticket.getCreator().getPlayer().getId().equals(id) && ticket.getState().is(states);
        }

        @Override
        public void validate(Object[] identifiers) {
            Validate.isTrue(identifiers.length == 2 && identifiers[0] instanceof UUID && identifiers[1] instanceof TicketState[]);
        }

        @Override
        public Ticket load(Object[] identifiers) {
            UUID creatorId = (UUID) identifiers[0];
            TicketState[] states = (TicketState[]) identifiers[1];

            QueryResultEntry resultEntry = dkSupport.getStorage().getTickets().find()
                    .where("CreatorId", creatorId)
                    .whereIn("State", Iterators.map(Arrays.asList(states), Enum::toString))
                    .execute().firstOrNull();
            if(resultEntry == null) return null;
            return new DefaultTicket(dkSupport,
                    resultEntry.getUniqueId("Id"),
                    resultEntry.getString("Topic"),
                    TicketState.parse(resultEntry.getString("State")),
                    resultEntry.getLong("Created"),
                    creatorId);
        }
    }
}
