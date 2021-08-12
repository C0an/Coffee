package cc.ryaan.coffee.profile.obj;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data @AllArgsConstructor
public class Note {

    private final UUID notedBy;
    private final String notedOn, notedReason;
    private long createdAt, removedAt;
    private UUID removedBy;
    private String removedOn, removedReason;


    public Note(UUID notedBy, String notedOn, String notedReason) {
        this.notedBy = notedBy;
        this.notedOn = notedOn;
        this.notedReason = notedReason;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isRemoved() {
        return removedBy != null;
    }

    public void remove(UUID uuid, String s, String removedOnServer) {
        this.removedBy = uuid;
        this.removedOn = removedOnServer;
        this.removedReason = s;
        this.removedAt = System.currentTimeMillis();
    }
}
