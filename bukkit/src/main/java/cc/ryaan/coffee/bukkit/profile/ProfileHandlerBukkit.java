package cc.ryaan.coffee.bukkit.profile;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.handler.ProfileHandler;
import cc.ryaan.coffee.profile.Profile;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.UUID;

public class ProfileHandlerBukkit extends ProfileHandler {

    public ProfileHandlerBukkit(Coffee coffee, MongoCollection<Document> profileCollection, Class<?> profileClass) {
        super(coffee, profileCollection, profileClass);
    }

    @Override
    public Profile createEmptyProfile(UUID uuid) {
        return new ProfileBukkit(uuid);
    }

}
