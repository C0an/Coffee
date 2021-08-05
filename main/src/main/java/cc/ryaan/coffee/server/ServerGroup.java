package cc.ryaan.coffee.server;

import lombok.Getter;
import org.bson.Document;

import java.util.Map;

@Getter
public class ServerGroup {

    private String groupName;
    private Map<String, String> announcements;

    public ServerGroup(String groupName) {
        this.groupName = groupName;
    }

    public ServerGroup(Document document) {

    }


}
