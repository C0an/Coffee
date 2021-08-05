package cc.ryaan.coffee.bukkit.populator;

import cc.ryaan.coffee.bukkit.CoffeeBukkit;
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
        return CoffeeBukkit.getInstance().getConfig().getString("mongo.database", "Coffee");
    }

    @Override
    public SystemType getSystemType() {
        return SystemType.BUKKIT;
    }

    @Override
    public JedisPool getJedisPool() {
        JedisBuilder jedisBuilder = JedisBuilder.getBuilder()
                .setHost(CoffeeBukkit.getInstance().getConfig().getString("redis.host", "127.0.0.1"))
                .setPort(CoffeeBukkit.getInstance().getConfig().getInt("redis.port", 6379))
                .setDbID(CoffeeBukkit.getInstance().getConfig().getInt("redis.database", 0));

        String password = CoffeeBukkit.getInstance().getConfig().getString("redis.password", "");
        if(password != null && !password.isEmpty()) jedisBuilder.setPassword(password);

        return jedisBuilder.build();
    }

    @Override
    public MongoClient getMongoDB() {
        MongoBuilder mongoBuilder = MongoBuilder.getBuilder()
                .setHost(CoffeeBukkit.getInstance().getConfig().getString("mongo.host", "127.0.0.1"))
                .setPort(CoffeeBukkit.getInstance().getConfig().getInt("mongo.port", 6379));

        String username = CoffeeBukkit.getInstance().getConfig().getString("mongo.username", "username");
        if(username != null && !username.isEmpty()) mongoBuilder.setUsername(username);

        String password = CoffeeBukkit.getInstance().getConfig().getString("mongo.password", "password");
        if(password != null && !password.isEmpty()) mongoBuilder.setPassword(password);

        String authDatabase = CoffeeBukkit.getInstance().getConfig().getString("mongo.authDatabase", "admin");
        if(authDatabase != null && !authDatabase.isEmpty()) mongoBuilder.setAuthDatabase(authDatabase);

        return mongoBuilder.build();
    }
}
