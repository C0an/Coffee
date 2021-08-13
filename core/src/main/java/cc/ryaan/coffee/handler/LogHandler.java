package cc.ryaan.coffee.handler;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import lombok.Getter;
import cc.ryaan.coffee.log.LoggerPopulator;

import java.util.ArrayList;
import java.util.List;

public class LogHandler {

    private final List<LoggerPopulator> providers = new ArrayList<>();
    @Getter private boolean initiated = false;


    public void init() {
        Preconditions.checkState(!this.initiated);
        this.initiated = true;
        registerProvider(new LoggerPopulator.DefaultLogger());
    }

    public void registerProvider(LoggerPopulator newProvider) {
        providers.add(newProvider);
        providers.sort((a, b) -> Ints.compare(b.getWeight(), a.getWeight()));
    }

    public LoggerPopulator getProvider(String name) {
        return providers.stream().filter(coffee -> coffee.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public LoggerPopulator getLoggerProvider() {
        return providers.get(0);
    }

}
