package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.Coffee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoHandler {

    private final Coffee coffee;
    private MongoDatabase database;
    private MongoCollection<Document> rankCollection, profileCollection, punishmentCollection;

    public MongoHandler(Coffee coffee) {
        this.coffee = coffee;
        try {
            this.database = coffee.getCoffeePopulator().getMongoDB().getDatabase(coffee.getCoffeePopulator().getMongoDatabase());
            this.rankCollection = this.database.getCollection("rank");
            this.profileCollection = this.database.getCollection("profile");
            this.punishmentCollection = this.database.getCollection("punishment");
        } catch (Exception e) {
            coffee.getLoggerPopulator().printLog("An error occurred whilst attempting to initialize the Mongo Handler.");
            coffee.getLoggerPopulator().printLog(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }


}
