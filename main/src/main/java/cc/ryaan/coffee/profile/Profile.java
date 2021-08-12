package cc.ryaan.coffee.profile;

import cc.ryaan.coffee.profile.obj.Grant;
import cc.ryaan.coffee.profile.obj.Note;
import cc.ryaan.coffee.rank.Rank;
import lombok.Data;

import java.util.*;

@Data
public abstract class Profile {

    private UUID uuid;
    private String username, prefix = "", suffix = "", color = "", connectedServer = "";
    private HashMap<String, String> permissions = new HashMap<>();

    private List<Grant> grants = new ArrayList<>();
    private HashSet<Note> notes = new HashSet<>();

    private long firstJoined = System.currentTimeMillis(), lastJoined = System.currentTimeMillis(), lastQuit = System.currentTimeMillis();
    private Map<String, String> metaData = new HashMap<>();

    private String currentIPAddress = "";
    private HashSet<String> previousIPAddresses = new HashSet<>();

    private transient List<String> activePermissions = new ArrayList<>();

    public Profile(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.username = uuid.toString();
    }

    public abstract List<Grant> getActiveGrants();
    public abstract Grant getCurrentGrant();
    public abstract void applyGrant(Grant grant, UUID executor, boolean shouldGetCurrentGrant);
    public abstract void refreshCurrentGrant();
    public abstract boolean isGrantActiveOnScope(Grant grant);
    public abstract Rank getRankFromGrant(Grant grant);


}
