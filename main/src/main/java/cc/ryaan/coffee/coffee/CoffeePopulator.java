package cc.ryaan.coffee.coffee;

import cc.ryaan.coffee.util.SystemType;
import com.mongodb.MongoClient;
import redis.clients.jedis.JedisPool;

import java.beans.ConstructorProperties;

public abstract class CoffeePopulator {

    private final String name;
    private final int weight;

    public abstract String getMongoDatabase();
    public abstract SystemType getSystemType();

    public abstract JedisPool getJedisPool();
    public abstract MongoClient getMongoDB();

    public abstract void shutdown();

    @ConstructorProperties(value={"name", "weight"})
    public CoffeePopulator(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CoffeePopulator)) return false;
        CoffeePopulator coffeePopulator = (CoffeePopulator)obj;
        return this.name.equals(coffeePopulator.getName()) && this.getSystemType().name().equals(coffeePopulator.getSystemType().name()) && this.weight == coffeePopulator.getWeight();
    }
}
