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

    /**
     * Initializes the Rank Handler by loading all the
     * ranks from MongoDB as well as create a
     * default rank if one is not found
     */
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
                    Rank defaultRank = new Rank("Default");
                    defaultRank.setDefaultRank(true);
                    defaultRank.setHidden(false);
                    saveRank(defaultRank, false);
                    ranks.add(defaultRank);
                    coffee.getLoggerPopulator().printLog("No default rank was found, it has been created.");
                }
            } catch (Exception ex) {
                coffee.getLoggerPopulator().printLog("Failed to load ranks from the database.");
                ex.printStackTrace();
            }
        }
        defaultGrant = new Grant(getDefaultRank().getUuid(), Long.MAX_VALUE, Collections.singletonList("GLOBAL"), "", "N/A", "Coffee");
    }

    /**
     * Gets a list of Rank Documents from MongoDB.
     *
     * @return {@link CompletableFuture}
     */
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

    /**
     * Sets a {@link Rank} as the cores default.
     *
     * @param defaultRank the {@link Rank} to make default.
     */
    public void setDefaultRank(Rank defaultRank) {
        getRanks().stream().filter(Rank::isDefaultRank).forEach(rank -> rank.setDefaultRank(false));
        defaultRank.setDefaultRank(true);
    }

    /**
     * Loads a {@link Rank} from MongoDB.
     *
     * @param uuid the {@link UUID} of the rank to load.
     * @param rankConsumer the {@link Consumer} which returns the loaded rank.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
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

    /**
     * Loads a {@link Rank} from a {@link Document}.
     *
     * @param document the {@link Document} to load data from.
     * @return the Rank containing all data from the Document.
     */
    public Rank loadRank(Document document) {
        return coffee.getGson().fromJson(document.toJson(), Rank.class);
    }

    /**
     * Saves a {@link Rank} to MongoDB.
     *
     * @param rank the {@link Rank} of the rank to save.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
    public void saveRank(Rank rank, boolean async) {
        if (async) {
            new Thread(() -> saveRank(rank, false)).start();
            return;
        }

        Document document = Document.parse(coffee.getGson().toJson(rank));
        UpdateResult updateResult = this.rankCollection.replaceOne(Filters.eq("uuid", rank.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        coffee.getLoggerPopulator().printLog(updateResult.wasAcknowledged() ? "Successfully saved the rank: " + rank.getDisplayName() : "Failed to save the rank: " + rank.getDisplayName());
    }

    /**
     * Deletes a {@link Rank} from MongoDB and Memory.
     *
     * @param uuid the {@link UUID} of the rank to delete.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
    public void deleteRank(UUID uuid, boolean async) {
        if(async) {
            new Thread(() -> deleteRank(uuid, false)).start();
            return;
        }
        Rank rank = getRank(uuid);
        if (rank == null) return;

        DeleteResult deleteResult = this.rankCollection.deleteOne(Filters.eq("uuid", rank.getUuid().toString()));
        this.getRanks().remove(rank);
        coffee.getLoggerPopulator().printLog(deleteResult.wasAcknowledged() ? "Successfully deleted the rank: " + rank.getDisplayName() : "Failed to delete the rank: " + rank.getDisplayName());
    }

    /**
     * Deletes a {@link Rank} from MongoDB and Memory.
     *
     * @param rank the {@link Rank} to delete.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
    public void deleteRank(Rank rank, boolean async) {
        deleteRank(rank.getUuid(), async);
    }

    /**
     * Finds a {@link Rank} that has <code>defaultRank</code> as <code>true</code>.
     *
     * @return the Rank that is the default.
     */
    public Rank getDefaultRank() {
        synchronized (ranks) {
            return ranks.stream().filter(Rank::isDefaultRank).findAny().orElse(null);
        }
    }

    /**
     * Finds a {@link Rank} from a {@link UUID}.
     *
     * @param uuid the {@link UUID} to find a rank by.
     * @return the Rank with a matching UUID.
     */
    public Rank getRank(UUID uuid) {
        return this.ranks.stream().filter(rank -> rank.getUuid().equals(uuid)).findAny().orElse(null);
    }

    /**
     * Finds a {@link Rank} from a {@link String}.
     *
     * @param string the {@link String} to find a rank by.
     * @return the Rank with a matching name.
     */
    public Rank getRank(String string) {
        return this.ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(string) || rank.getDisplayName().equalsIgnoreCase(string)).findAny().orElse(null);
    }

}
