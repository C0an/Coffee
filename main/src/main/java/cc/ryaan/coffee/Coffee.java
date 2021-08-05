package cc.ryaan.coffee;

import cc.ryaan.coffee.coffee.CoffeePopulator;
import cc.ryaan.coffee.handler.*;
import cc.ryaan.coffee.log.LoggerPopulator;
import lombok.Getter;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Getter
public class Coffee {

    private final CoffeePopulator coffeePopulator;
    private final LoggerPopulator loggerPopulator;
    private CoffeeHandler coffeeHandler;
    private LogHandler logHandler;
    private MongoHandler mongoHandler;
    private ServerHandler serverHandler;
    private RankHandler rankHandler;

    public Coffee(CoffeePopulator coffeePopulator, LoggerPopulator loggerPopulator) {
        this.coffeePopulator = coffeePopulator;
        this.loggerPopulator = loggerPopulator;
        init();
    }

    protected void init() {
        loggerPopulator.printLog("Initialising Coffee using the populator \"" + coffeePopulator.getName() + "\" with logger \"" + loggerPopulator.getName() + "\".");
        loggerPopulator.printLog("Attempting to connect to Redis...");

        if(!setupRedis()) {
            loggerPopulator.printLog("Failed to connect to Redis - please correct the issue and reinitialise!");
            return;
        } else loggerPopulator.printLog("Successfully connected to Redis!");


        loggerPopulator.printLog("Attempting to connect to Mongo...");
        if(!setupMongo()) {
            loggerPopulator.printLog("Failed to connect to Mongo - please correct the issue and reinitialise!");
            return;
        } else loggerPopulator.printLog("Successfully connected to Mongo!");


        (this.coffeeHandler = new CoffeeHandler()).init();
        (this.logHandler = new LogHandler()).init();
        this.mongoHandler = new MongoHandler(this);
        this.serverHandler = new ServerHandler(this);
        this.rankHandler = new RankHandler(this, mongoHandler.getRankCollection());
        loggerPopulator.printLog("We're ready for requests!");
    }

    protected boolean setupRedis() {
        if(coffeePopulator.getJedisPool() == null || coffeePopulator.getJedisPool().isClosed()) return false;
        try {
            coffeePopulator.getJedisPool().getResource();
        }catch (JedisConnectionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected boolean setupMongo() {
        if(coffeePopulator.getMongoDB() == null) return false;
        try {
            coffeePopulator.getMongoDB().getAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
