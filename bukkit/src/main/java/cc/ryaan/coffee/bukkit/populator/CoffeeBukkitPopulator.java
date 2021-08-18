package cc.ryaan.coffee.bukkit.populator;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.coffee.CoffeePopulator;
import cc.ryaan.coffee.util.JedisBuilder;
import cc.ryaan.coffee.util.MongoBuilder;
import cc.ryaan.coffee.util.SystemType;
import com.mongodb.MongoClient;
import redis.clients.jedis.JedisPool;

public class CoffeeBukkitPopulator extends CoffeePopulator {

    public CoffeeBukkitPopulator() {
        super("Coffee - Bukkit", 100);
    }

    @Override
    public String getMongoDatabase() {
        return CoffeeBukkitPlugin.getInstance().getConfig().getString("mongo.database", "Coffee");
    }

    @Override
    public SystemType getSystemType() {
        return SystemType.BUKKIT;
    }

    @Override
    public JedisPool getJedisPool() {
        JedisBuilder jedisBuilder = JedisBuilder.getBuilder()
                .setHost(CoffeeBukkitPlugin.getInstance().getConfig().getString("redis.host", "127.0.0.1"))
                .setPort(CoffeeBukkitPlugin.getInstance().getConfig().getInt("redis.port", 6379))
                .setDbID(CoffeeBukkitPlugin.getInstance().getConfig().getInt("redis.database", 0))
                .setAuthorization(CoffeeBukkitPlugin.getInstance().getConfig().getBoolean("redis.auth.enabled", false))
                .setTimeout(CoffeeBukkitPlugin.getInstance().getConfig().getInt("redis.timeout", 3000));

        String password = CoffeeBukkitPlugin.getInstance().getConfig().getString("redis.password", "");
        if(password != null && !password.isEmpty()) jedisBuilder.setPassword(password);

        return jedisBuilder.build();
    }

    @Override
    public MongoClient getMongoDB() {
        MongoBuilder mongoBuilder = MongoBuilder.getBuilder()
                .setHost(CoffeeBukkitPlugin.getInstance().getConfig().getString("mongo.host", "127.0.0.1"))
                .setPort(CoffeeBukkitPlugin.getInstance().getConfig().getInt("mongo.port", 6379));

        if(CoffeeBukkitPlugin.getInstance().getConfig().getBoolean("mongo.auth.enabled", false)) {
            String username = CoffeeBukkitPlugin.getInstance().getConfig().getString("mongo.auth.username", "username");
            if(username != null && !username.isEmpty()) mongoBuilder.setUsername(username);

            String password = CoffeeBukkitPlugin.getInstance().getConfig().getString("mongo.auth.password", "password");
            if(password != null && !password.isEmpty()) mongoBuilder.setPassword(password);

            String authDatabase = CoffeeBukkitPlugin.getInstance().getConfig().getString("mongo.auth.authDatabase", "admin");
            if(authDatabase != null && !authDatabase.isEmpty()) mongoBuilder.setAuthDatabase(authDatabase);
        }

        return mongoBuilder.build();
    }

    @Override
    public void shutdown() {
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRanks().forEach(rank -> CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().saveRank(rank, false));
    }
}
