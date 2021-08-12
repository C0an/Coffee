package cc.ryaan.coffee.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class MongoBuilder {

    private String host = "127.0.0.1", username, password, authDatabase = "admin";
    private int port = 27017;

    public static MongoBuilder getBuilder() {
        return new MongoBuilder();
    }

    public MongoBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public MongoBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public MongoBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MongoBuilder setAuthDatabase(String authDatabase) {
        this.authDatabase = authDatabase;
        return this;
    }

    public MongoBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public MongoClient build() {
        return new MongoClient(new MongoClientURI("mongodb://" + (getUsername() != null && !getUsername().equals("") ? (getPassword().equals("") ? "" : getUsername() + ":" + getPassword() + "@") : "") + getHost() + ":" + getPort() + (getAuthDatabase().equals("") ? "" : "/" + getAuthDatabase())));
    }
}
