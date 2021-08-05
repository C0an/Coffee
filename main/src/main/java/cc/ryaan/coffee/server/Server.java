package cc.ryaan.coffee.server;

import cc.ryaan.coffee.util.StatusType;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter @AllArgsConstructor
public class Server {

    private final String serverName;
    private final String group;
    @Setter private StatusType statusType;
    @Setter private List<String> connections;
    @Setter private long lastHeartbeat;
    @Setter private JsonObject serverData;

    public Server(String serverName, String group, StatusType statusType) {
        this.serverName = serverName;
        this.group = group;
        this.statusType = statusType;
    }

    public int getOnline() {
        return connections.size();
    }

    public boolean isOnline() {
        return System.currentTimeMillis() - lastHeartbeat <= TimeUnit.SECONDS.toMillis(5);
    }

}
