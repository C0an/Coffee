package cc.ryaan.coffee.handler;

import cc.ryaan.coffee.coffee.CoffeePopulator;
import cc.ryaan.coffee.util.JedisBuilder;
import cc.ryaan.coffee.util.MongoBuilder;
import cc.ryaan.coffee.util.SystemType;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.mongodb.MongoClient;
import lombok.Getter;
import redis.clients.jedis.JedisPool;
import cc.ryaan.coffee.Coffee;

import java.util.ArrayList;
import java.util.List;

public class CoffeeHandler {

    private Coffee coffee;
    private final List<CoffeePopulator> providers = new ArrayList<>();
    @Getter private boolean initiated = false;


    public void init() {
        Preconditions.checkState(!this.initiated);
        this.initiated = true;
        registerProvider(new DefaultCoffeeProvider());
    }

    public void registerProvider(CoffeePopulator newProvider) {
        providers.add(newProvider);
        providers.sort((a, b) -> Ints.compare(b.getWeight(), a.getWeight()));
    }

    public CoffeePopulator getProvider(String name) {
        return providers.stream().filter(coffee -> coffee.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public CoffeePopulator getCoffeeProvider() {
        return providers.get(0);
    }

    private static class DefaultCoffeeProvider extends CoffeePopulator {

        private DefaultCoffeeProvider() {
            super("Custom Applet", 0);
        }

        @Override
        public String getMongoDatabase() {
            return "Coffee";
        }

        @Override
        public SystemType getSystemType() {
            return SystemType.OTHER;
        }

        @Override
        public JedisPool getJedisPool() {
            return new JedisBuilder().build();
        }

        @Override
        public MongoClient getMongoDB() {
            try {
                return new MongoBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void shutdown() {

        }
    }

}
