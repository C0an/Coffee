package cc.ryaan.coffee.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter @NoArgsConstructor
public class JedisBuilder {

    private String host = "127.0.0.1", password = "";
    private int port = 6379, dbID = 0, timeout = 3000;

    public static JedisBuilder getBuilder() {
        return new JedisBuilder();
    }

    public JedisBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public JedisBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public JedisBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public JedisBuilder setDbID(int dbID) {
        this.dbID = dbID;
        return this;
    }

    public JedisBuilder setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public JedisPool build() {
        return new JedisPool(new JedisPoolConfig(), getHost(), getPort(), getTimeout(), getPassword(), getDbID());
    }
}
