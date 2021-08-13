package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.profile.Profile;
import cc.ryaan.coffee.profile.obj.Grant;
import cc.ryaan.coffee.profile.obj.LoadType;
import cc.ryaan.coffee.rank.Rank;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.Getter;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
public abstract class ProfileHandler {

    private final Coffee coffee;
    private final MongoCollection<Document> profileCollection;
    private final Map<UUID, Object> profiles = new HashMap<>();
    private Class<?> profileClass;

    public ProfileHandler(Coffee coffee, MongoCollection<Document> profileCollection, Class<?> profileClass) {
        this.coffee = coffee;
        this.profileCollection = profileCollection;
        this.profileClass = profileClass;
    }

    /**
     * Initializes the Profile Handler by clearing
     * all the profiles from Memory.
     */
    public void init() {
        synchronized (profiles) {
            profiles.clear();
        }
    }

    /**
     * Creates a {@link Profile} from a {@link UUID}.
     *
     * @param uuid the {@link UUID} to create a Profile with.
     * @return an empty profile with a UUID.
     */
    public abstract Profile createEmptyProfile(UUID uuid);

    /**
     * Gets a list of Profile Documents from MongoDB.
     *
     * @return {@link CompletableFuture}
     */
    public CompletableFuture<List<Document>> getProfileDocumentsFromDB() {
        CompletableFuture<List<Document>> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                List<Document> documents = new ArrayList<>();
                this.profileCollection.find().forEach((Consumer<Document>) documents::add);
                future.complete(documents);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        }).start();

        return future;
    }

    /**
     * Loads a {@link Object} from a {@link Document}.
     *
     * @param document the {@link Document} to load data from.
     * @return the Profile Object containing all data from the Document.
     */
    public Object loadProfile(Document document) {
        JsonWriterSettings relaxed = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
        return coffee.getGson().fromJson(document.toJson(relaxed), profileClass);
    }

    /**
     * Loads a {@link Object} from MongoDB.
     *
     * @param input the {@link String} of the input to search for.
     * @param callback the {@link Consumer} which returns the loaded profile object.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     * @param loadType the {@link LoadType} that defines what type of data to search for.
     */
    public void loadProfile(String input, Consumer<Object> callback, boolean async, LoadType loadType) {
        if(async) {
            new Thread(() -> loadProfile(input, callback, false, loadType)).start();
            return;
        }
        if(loadType == LoadType.UUID) {
            try {
                UUID uuid = UUID.fromString(input);
            }catch (Exception e) {
                callback.accept(null);
                return;
            }
        }

        Document document = profileCollection.find(Filters.regex(loadType.getObjectName(), Pattern.compile("^" + input + "$", Pattern.CASE_INSENSITIVE))).first();
        if (document == null) {
            callback.accept(null);
            return;
        }

        callback.accept(loadProfile(document));
    }

    /**
     * Saves a {@link Profile} to MongoDB.
     *
     * @param profileObject the {@link Object} of the profile to save.
     * @param callback the {@link Consumer} which returns whether deletion was successful.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
    public void saveProfile(Object profileObject, Consumer<Boolean> callback, boolean async) {
        if (async) {
            new Thread(() -> saveProfile(profileObject, callback, false)).start();
            return;
        }
        Profile profile = (Profile) profileClass.cast(profileObject);

        Document document = Document.parse(coffee.getGson().toJson(profile));
        UpdateResult updateResult = this.profileCollection.replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
        coffee.getLoggerPopulator().printLog(updateResult.wasAcknowledged() ? "Successfully saved the profile of: " + profile.getUsername() : "Failed to save the profile of: " + profile.getUsername());
        callback.accept(updateResult.wasAcknowledged());
    }

    /**
     * Deletes a {@link Profile} from MongoDB.
     *
     * @param uuid the {@link UUID} of the profile to delete.
     * @param callback the {@link Consumer} which returns whether deletion was successful.
     * @param async the {@link Boolean} that defines whether to load on a new thread.
     */
    public void deleteProfile(UUID uuid, Consumer<Boolean> callback, boolean async) {
        if (async) {
            new Thread(() -> deleteProfile(uuid, callback, false)).start();
            return;
        }
        DeleteResult deleteResult = profileCollection.deleteOne(Filters.eq("uuid", uuid.toString()));
        coffee.getLoggerPopulator().printLog(deleteResult.wasAcknowledged() ? "Successfully deleted the profile of: " + uuid : "Failed to delete the profile of: " + uuid);
        callback.accept(deleteResult.wasAcknowledged());
    }

    /**
     * Finds a {@link Object} from a {@link UUID}.
     *
     * @param uuid the {@link UUID} to find a profile by.
     * @return the Profile Object with a matching UUID.
     */
    public Object getProfileByUUID(UUID uuid) {
        if (uuid == null) return null;
        return profileClass.cast(profiles.get(uuid));
    }

    /**
     * Adds a {@link Object} from a {@link UUID} and {@link Object}.
     *
     * @param uuid the {@link UUID} to identify a profile by.
     * @param profile the {@link Object} to store the profile in memory.
     * @return the Profile Object imported.
     */
    public Object addProfile(UUID uuid, Object profile) {
        profiles.put(uuid, profile);
        return profile;
    }

    /**
     * Saves all {@link Profile} in Memory to MongoDB.
     */
    public void save() {
        for (Object profile : getProfiles()) {
            saveProfile(profile, callback -> {}, true);
        }
    }

    /**
     * Gives a {@link List} of profiles from the profile {@link Map}.
     *
     * @return the list of Profile Objects in Memory.
     */
    public List<Object> getProfiles() {
        return new ArrayList<>(profiles.values());
    }

    /**
     * Finds a {@link Object} from a {@link UUID}.
     *
     * @param uuid the {@link UUID} to find a profile or create with.
     * @return the Profile Object loaded/created.
     */
    public Object getProfileByUUIDOrCreate(UUID uuid) {
        if(uuid == null) return null;
        AtomicReference<Object> prof = new AtomicReference<>(getProfileByUUID(uuid));
        if (prof.get() == null) {
            try {
                loadProfile(uuid.toString(), prof::set, false, LoadType.UUID);
            } catch (Exception ex) {
                ex.printStackTrace();
                Profile profile = createEmptyProfile(uuid);
                profile.applyGrant(coffee.getRankHandler().getDefaultGrant(), null, false);
                return profile;
            }
        }
        return prof.get();
    }

}
