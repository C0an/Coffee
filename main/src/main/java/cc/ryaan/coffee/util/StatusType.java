package cc.ryaan.coffee.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum StatusType {

    ONLINE("Online"),
    WHITELISTED("Whitelisted"),
    BOOTING("Booting"),
    OFFLINE("Offline");

    private final String displayName;

}
