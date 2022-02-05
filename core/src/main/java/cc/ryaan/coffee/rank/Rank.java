package cc.ryaan.coffee.rank;

import cc.ryaan.coffee.server.ServerGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor @Data
@Builder(builderMethodName = "internalBuilder")
public class Rank {

    private UUID uuid;
    private String name, displayName;
    @Builder.Default private String colour = "§a", prefix = "§a", suffix = "§a";
    @Builder.Default private int priority = 0;
    @Builder.Default private boolean defaultRank = false, staff = false, hidden = true;
    // Here we do a Class as we have ServerGroup and Server[String] specific permissions.
    @Builder.Default private Map<Object, String> permissions = new HashMap<>();
    @Builder.Default private Map<String, String> metadata = new HashMap<>();

    @Builder
    public Rank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.displayName = name;
    }

    public String getColouredName() {
        return getColour() + getDisplayName();
    }

    public static RankBuilder builder(String name) {
        return internalBuilder().uuid(UUID.randomUUID()).name(name).displayName(name);
    }

}
