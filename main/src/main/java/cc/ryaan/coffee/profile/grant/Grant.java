package cc.ryaan.coffee.profile.grant;

import cc.ryaan.coffee.rank.Rank;
import cc.ryaan.coffee.util.TimeUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
public class Grant {

    private final UUID uuid;
    private final Rank rank;
    private final long length, initialTime;
    private final List<Object> scope;
    private final String reason, grantedBy, grantedOn;

    @Setter private boolean removed;
    @Setter private long removedAt;
    @Setter private String removedReason, removedBy, removedOn;

    public Grant(Rank rank, long length, List<Object> scope, String reason, String grantedBy, String grantedOn) {
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

    public Grant(UUID uuid, Rank rank, long length, long initialTime, List<Object> scope, String reason, String grantedBy, String grantedOn) {
        this.uuid = uuid;
        this.rank = rank;
        this.length = length;
        this.initialTime = initialTime;
        this.scope = scope;
        this.reason = reason;
        this.grantedBy = grantedBy;
        this.grantedOn = grantedOn;
    }

    public Grant(UUID uuid, Rank rank, long length, long initialTime, List<Object> scope, String reason, String grantedBy, String grantedOn, boolean removed, long removedAt, String removedReason, String removedBy, String removedOn) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mmaa");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return dateFormat.format(cal.getTime()) + " (" + TimeUtil.getTimeZoneShortName(cal.getTimeZone().getDisplayName()) + ")";
    }

    public long getActiveUntil() {
        return length == Long.MAX_VALUE ? Long.MAX_VALUE : (initialTime + length);
    }

    public boolean isStillActive() {
        return getActiveUntil() > System.currentTimeMillis() && !removed;
    }

}
