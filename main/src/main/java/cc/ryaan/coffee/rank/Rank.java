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

@AllArgsConstructor @Data @Builder
public class Rank {

    private UUID uuid;
    private String name, displayName, colour = "§a", prefix = "§a", suffix = "§a";
    private int displayPriority = 0, orderPriority = 0;
    private boolean defaultRank, hidden;
    // Here we do a Class as we have ServerGroup and Server[String] specific permissions.
    private Map<Object, String> permissions = new HashMap<>();

    public Rank(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.displayName = name;
        this.defaultRank = false;
        this.hidden = true;
    }

    public String getColouredName() {
        return getColour() + getDisplayName();
    }

}
