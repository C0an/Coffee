package cc.ryaan.coffee.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.rank.Rank;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Getter
public class RankHandler {

    public final Coffee coffee;
    public final MongoCollection<Document> rankCollection;
    public final Set<Rank> ranks = new HashSet<>();

    public RankHandler(Coffee coffee, MongoCollection<Document> rankCollection) {
        this.coffee = coffee;
        this.rankCollection = rankCollection;
    }

    public void init() {
        ranks.clear();

        getRanksInDatabase(callback -> {
            coffee.getLoggerPopulator().printLog("§aFound " + callback.size() + " ranks in database.");
            List<Rank> rankList = new ArrayList<>();
            AtomicInteger done = new AtomicInteger();

            for (UUID uuid : callback) {
                loadRank(uuid, rankConsumer -> {
                    rankList.add(rankConsumer);
                    done.getAndIncrement();
                }, false);
            }

            ranks.addAll(rankList);
        });
    }

    public void getRanksInDatabase(Consumer<Set<UUID>> consumer) {
        Set<UUID> uuidSet = new HashSet<>();
        for (Document document : this.rankCollection.find()) uuidSet.add(UUID.fromString(document.getString("uuid")));
        consumer.accept(uuidSet);
    }

    public void loadRank(UUID uuid, Consumer<Rank> rankConsumer, boolean async) {
        if(async) {
            new Thread(() -> loadRank(uuid, rankConsumer, false)).start();
            return;
        }
        if(getRank(uuid) != null) {
            rankConsumer.accept(getRank(uuid));
            return;
        }
        Document document = this.rankCollection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            rankConsumer.accept(null);
            return;
        }

        rankConsumer.accept(new Rank(document));
    }

    public void saveRank(Rank rank, boolean async) {
        if (async) {
            new Thread(() -> saveRank(rank, false)).start();
            return;
        }

        Document document = new Document()
                .append("uuid", rank.getUuid().toString())
                .append("name", rank.getName())
                .append("displayName", rank.getDisplayName())
                .append("color", rank.getColor())
                .append("prefix", rank.getPrefix())
                .append("suffix", rank.getSuffix())
                .append("displayPriority", rank.getDisplayPriority())
                .append("orderPriority", rank.getOrderPriority())
                .append("permissions", new Gson().toJson(rank.getPermissions()));

        this.rankCollection.replaceOne(Filters.eq("uuid", rank.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    public void deleteRank(UUID id, boolean async) {
        if(async) {
            new Thread(() -> deleteRank(id, false)).start();
            return;
        }
        Rank rank = getRank(id);
        if (rank == null) return;

        this.rankCollection.deleteOne(Filters.eq("uuid", rank.getUuid().toString()));
        this.getRanks().remove(rank);
    }

    public void deleteRank(Rank rank, boolean async) {
        deleteRank(rank.getUuid(), async);
    }

    public Rank getRank(UUID uuid) {
        return this.ranks.stream().filter(rank -> rank.getUuid().equals(uuid)).findAny().orElse(null);
    }

    public Rank getRank(String string) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(string) || rank.getDisplayName().equalsIgnoreCase(string)).findAny().orElse(null);
    }

}
