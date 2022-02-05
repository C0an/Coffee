package cc.ryaan.coffee.profile.obj;

import cc.ryaan.coffee.util.TimeUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter @Builder
public class Grant {

    private final UUID uuid;
    private final UUID rank;
    private final long length, initialTime;
    private final List<String> scope;
    private final String reason, grantedBy, grantedOn;

    @Setter private boolean removed;
    @Setter private long removedAt;
    @Setter private String removedReason, removedBy, removedOn;

    public Grant(UUID rank, long length, List<String> scope, String reason, String grantedBy, String grantedOn) {
        this.uuid = UUID.randomUUID();
        this.rank = rank;
        this.scope = scope;
        this.length = length;
        this.initialTime = System.currentTimeMillis();
        this.reason = reason;
        this.grantedBy = grantedBy;
        this.grantedOn = grantedOn;
        this.removed = false;
        this.removedAt = -1L;
        this.removedBy = "";
        this.removedOn = "";
        this.removedReason = "";
    }

    public Grant(UUID uuid, UUID rank, long length, long initialTime, List<String> scope, String reason, String grantedBy, String grantedOn) {
        this.uuid = uuid;
        this.rank = rank;
        this.length = length;
        this.initialTime = initialTime;
        this.scope = scope;
        this.reason = reason;
        this.grantedBy = grantedBy;
        this.grantedOn = grantedOn;
    }

    public Grant(UUID uuid, UUID rank, long length, long initialTime, List<String> scope, String reason, String grantedBy, String grantedOn, boolean removed, long removedAt, String removedReason, String removedBy, String removedOn) {
        this.uuid = uuid;
        this.rank = rank;
        this.length = length;
        this.initialTime = initialTime;
        this.scope = scope;
        this.reason = reason;
        this.grantedBy = grantedBy;
        this.grantedOn = grantedOn;
        this.removed = removed;
        this.removedAt = removedAt;
        this.removedReason = removedReason;
        this.removedBy = removedBy;
        this.removedOn = removedOn;
    }

    public String formatGrantedTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return new SimpleDateFormat("dd/MM/yy HH:mmaa").format(cal.getTime()) + " (" + TimeUtil.getTimeZoneShortName(cal.getTimeZone().getDisplayName()) + ")";
    }

    public long getActiveUntil() {
        return length == Long.MAX_VALUE ? Long.MAX_VALUE : (initialTime + length);
    }

    public boolean isStillActive() {
        return getActiveUntil() > System.currentTimeMillis() && !removed;
    }

}
