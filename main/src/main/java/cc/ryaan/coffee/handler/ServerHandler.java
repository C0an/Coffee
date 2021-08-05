package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.server.Server;
import cc.ryaan.coffee.server.ServerGroup;
import cc.ryaan.coffee.util.StatusType;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import cc.ryaan.coffee.Coffee;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ServerHandler {

    private final Coffee coffee;
    private final Set<ServerGroup> serverGroups = new HashSet<>();
    private final Set<Server> servers = new HashSet<>();

    public ServerHandler(Coffee coffee) {
        this.coffee = coffee;
    }

    public Server getServer(String name) {
        return servers.stream().filter(server -> server.getServerName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public List<Server> getServers(String group) {
        return servers.stream().filter(server -> server.getGroup().equalsIgnoreCase(group)).collect(Collectors.toList());
    }

    // Credit to vaperion
    private void grabServers() {
        try (Jedis jedis = coffee.getCoffeePopulator().getJedisPool().getResource()) {
            for (String keyName : jedis.keys("servers:*")) {
                Map<String, String> data = jedis.hgetAll(keyName);
                setStatus(
                        keyName.split(":")[1],
                        data.get("serverGroup"),
                        data.get("statusType"),
                        new Gson().fromJson(data.get("connections"), new TypeToken<List<String>>() {}.getType()),
                        Long.parseLong(data.get("lastHeartbeat")),
                        (JsonObject) JsonParser.parseString(data.get("serverData"))
                );
            }
        }
    }

    private void setStatus(String serverName, String serverGroup, String serverStatus, List<String> connections, long lastHeartbeat, JsonObject serverData) {
        StatusType statusType;

        try { statusType = StatusType.valueOf(serverStatus);
        }catch (Exception e) { statusType = StatusType.ONLINE; }

        Server server = getServer(serverName);
        if(server == null) server = new Server(serverName, serverGroup, statusType);

        server.setConnections(connections);
        server.setLastHeartbeat(lastHeartbeat);
        server.setServerData(serverData);

        servers.add(server);
    }

    private void update(Server server) {
        try (Jedis jedis = coffee.getCoffeePopulator().getJedisPool().getResource()) {
            jedis.hset("status:" + server.getServerName(), "serverGroup", server.getGroup());
            jedis.hset("status:" + server.getServerName(), "statusType", server.getStatusType().name());
            jedis.hset("status:" + server.getServerName(), "connections", new Gson().toJson(server.getConnections()));
            jedis.hset("status:" + server.getServerName(), "lastHeartbeat", String.valueOf(server.getLastHeartbeat()));
            jedis.hset("status:" + server.getServerName(), "serverData", server.getServerData().toString());
        }
    }


}
