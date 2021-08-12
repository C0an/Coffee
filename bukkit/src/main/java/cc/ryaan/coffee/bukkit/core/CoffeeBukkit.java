package cc.ryaan.coffee.bukkit.core;

import cc.ryaan.coffee.Coffee;
import cc.ryaan.coffee.bukkit.profile.ProfileBukkit;
import cc.ryaan.coffee.bukkit.profile.ProfileHandlerBukkit;
import cc.ryaan.coffee.coffee.CoffeePopulator;
import cc.ryaan.coffee.handler.ProfileHandler;
import cc.ryaan.coffee.log.LoggerPopulator;
import lombok.Getter;

@Getter
public class CoffeeBukkit extends Coffee {

    private ProfileHandler profileHandler;

    public CoffeeBukkit(CoffeePopulator coffeePopulator, LoggerPopulator loggerPopulator) {
        super(coffeePopulator, loggerPopulator);
    }

    @Override
    protected void init() {
        super.init();
        (this.profileHandler = new ProfileHandlerBukkit(this, getMongoHandler().getProfileCollection(), ProfileBukkit.class)).init();
    }
}
