package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.profile.Profile;
import cc.ryaan.coffee.profile.obj.Grant;
import cc.ryaan.coffee.profile.obj.LoadType;
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

    public void init() {
        synchronized (profiles) {
            profiles.clear();
        }
    }

    public abstract Profile createEmptyProfile(UUID uuid);

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

    public Object loadProfile(Document document) {
        JsonWriterSettings relaxed = JsonWriterSettings.builder().outputMode(JsonMode.RELAXED).build();
        return coffee.getGson().fromJson(document.toJson(relaxed), profileClass);
    }

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

    public void deleteProfile(UUID uuid, Consumer<Boolean> callback, boolean async) {
        if (async) {
            new Thread(() -> deleteProfile(uuid, callback, false)).start();
            return;
        }
        DeleteResult deleteResult = profileCollection.deleteOne(Filters.eq("uuid", uuid.toString()));
        coffee.getLoggerPopulator().printLog(deleteResult.wasAcknowledged() ? "Successfully deleted the profile of: " + uuid : "Failed to delete the profile of: " + uuid);
        callback.accept(deleteResult.wasAcknowledged());
    }

    public Object getProfileByUUID(UUID id) {
        if (id == null) return null;
        return profileClass.cast(profiles.get(id));
    }

    public Object addProfile(UUID uuid, Object profile) {
        profiles.put(uuid, profile);
        return profile;
    }

    public void save() {
        for (Object profile : getProfiles()) {
            saveProfile(profile, callback -> {}, true);
        }
    }

    public List<Object> getProfiles() {
        return new ArrayList<>(profiles.values());
    }

    public Object getProfileByUUIDOrCreate(UUID id) {
        if(id == null) return null;
        AtomicReference<Object> prof = new AtomicReference<>(getProfileByUUID(id));
        if (prof.get() == null) {
            try {
                loadProfile(id.toString(), prof::set, false, LoadType.UUID);
            } catch (Exception ex) {
                ex.printStackTrace();
                Profile profile = createEmptyProfile(id);
                profile.applyGrant(coffee.getRankHandler().getDefaultGrant(), null, false);
                return profile;
            }
        }
        return prof.get();
    }
//
//    public Profile getProfileByUsernameOrCreate(String id) {
//        if(id == null) return null;
//        AtomicReference<Profile> prof = new AtomicReference<>(getProfileByUsername(id));
//        if (prof.get() == null) {
//            try {
//                loadProfile(id, prof::set, false, LoadType.USERNAME);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return null;
//            }
//        }
//        return prof.get();
//    }
//
//    public Profile getNewProfileOrCreate(String name, UUID op) {
//
//        Profile prof = getProfileByUUIDOrCreate(op);
//        if(prof == null) {
//            addProfile(new Profile(name, op,false)).applyGrant(Grant.getDefaultGrant(), null, false);
//            prof = getProfileByUUID(op);
//        }
//        return prof;
//    }

}
