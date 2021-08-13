package cc.ryaan.coffee.profile.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoadType {

    USERNAME("name"),
    IP("currentIP"),
    UUID("uuid");

    private final String objectName;

}