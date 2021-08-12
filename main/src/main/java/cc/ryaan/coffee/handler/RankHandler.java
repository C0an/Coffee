package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.profile.obj.Grant;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import org.bson.Document;
import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.rank.Rank;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Getter
public class RankHandler {

    private final Coffee coffee;
    private final MongoCollection<Document> rankCollection;
    private final Set<Rank> ranks = new HashSet<>();
    private Grant defaultGrant;

    public RankHandler(Coffee coffee, MongoCollection<Document> rankCollection) {
        this.coffee = coffee;
        this.rankCollection = rankCollection;
    }

    public void init() {
        synchronized (ranks) {
            ranks.clear();

            try {
                List<Document> documents = getRankDocumentsFromDB().join();
                if (documents == null) throw new IllegalStateException("No ranks were returned");

                for (Document document : documents) {
                    UUID uuid = UUID.fromString(document.getString("uuid"));

                    Rank rank = getRank(uuid);
                    if (rank == null)
                        rank = loadRank(document);

                    if (rank == null)
                        throw new IllegalStateException("Failed to load rank data for rank '" + document.getString("name") + "'");

                    ranks.add(rank);
                    System.out.println(ranks);
                }

                coffee.getLoggerPopulator().printLog("Successfully imported " + ranks.size() + " ranks from the database.");

                if (getDefaultRank() == null) {
                    ranks.add(Rank.builder().uuid(UUID.randomUUID()).name("Default").displayName("Default").priority(0).hidden(false).colour("§a").prefix("§a").suffix("§a").permissions(new HashMap<>()).defaultRank(true).build());
                    coffee.getLoggerPopulator().printLog("No default rank was found, it has been created.");
                }
            } catch (Exception ex) {
                coffee.getLoggerPopulator().printLog("Failed to load ranks from the database.");
                ex.printStackTrace();
            }
        }
        defaultGrant = new Grant(getDefaultRank().getUuid(), Long.MAX_VALUE, Collections.singletonList("GLOBAL"), "", "N/A", "Coffee");
    }

    public CompletableFuture<List<Document>> getRankDocumentsFromDB() {
        CompletableFuture<List<Document>> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                List<Document> documents = new ArrayList<>();
                this.rankCollection.find().forEach((Consumer<Document>) documents::add);
                future.complete(documents);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        }).start();

        return future;
    }

    public void loadRank(UUID uuid, Consumer<Rank> rankConsumer, boolean async) {
        if(async) {
            new Thread(() -> loadRank(uuid, rankConsumer, false)).start();
            return;
        }

        Document document = this.rankCollection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            rankConsumer.accept(null);
            return;
        }

        rankConsumer.accept(loadRank(document));
    }

    public Rank loadRank(Document document) {
        return coffee.getGson().fromJson(document.toJson(), Rank.class);
    }

    public void saveRank(Rank rank, boolean async) {
        if (async) {
            new Thread(() -> saveRank(rank, false)).start();
            return;
        }

        Document document = Document.parse(coffee.getGson().toJson(rank));
        UpdateResult updateResult = this.rankCollection.replaceOne(Filters.eq("uuid", rank.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        coffee.getLoggerPopulator().printLog(updateResult.wasAcknowledged() ? "Successfully saved the rank: " + rank.getDisplayName() : "Failed to save the rank: " + rank.getDisplayName());
    }

    public void deleteRank(UUID id, boolean async) {
        if(async) {
            new Thread(() -> deleteRank(id, false)).start();
            return;
        }
        Rank rank = getRank(id);
        if (rank == null) return;

        DeleteResult deleteResult = this.rankCollection.deleteOne(Filters.eq("uuid", rank.getUuid().toString()));
        this.getRanks().remove(rank);
        coffee.getLoggerPopulator().printLog(deleteResult.wasAcknowledged() ? "Successfully deleted the rank: " + rank.getDisplayName() : "Failed to delete the rank: " + rank.getDisplayName());

    }

    public void deleteRank(Rank rank, boolean async) {
        deleteRank(rank.getUuid(), async);
    }

    public Rank getDefaultRank() {
        synchronized (ranks) {
            return ranks.stream().filter(Rank::isDefaultRank).findAny().orElse(null);
        }
    }

    public Rank getRank(UUID uuid) {
        return this.ranks.stream().filter(rank -> rank.getUuid().equals(uuid)).findAny().orElse(null);
    }

    public Rank getRank(String string) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(string) || rank.getDisplayName().equalsIgnoreCase(string)).findAny().orElse(null);
    }

}
