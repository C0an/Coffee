package cc.ryaan.coffee.bukkit.profile;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.profile.Profile;
import cc.ryaan.coffee.profile.obj.Grant;
import cc.ryaan.coffee.rank.Rank;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ProfileBukkit extends Profile {

    public ProfileBukkit(UUID uuid, String username) {
        super(uuid, username);
    }

    public ProfileBukkit(UUID uuid) {
        super(uuid, uuid.toString());
    }

    @Override
    public List<Grant> getActiveGrants() {
        List<Grant> active = new ArrayList<>();

        if (getGrants() == null || getGrants().isEmpty())
            return active;

        getGrants().removeIf(grant ->
                grant.getRank() == null ||
                getRankFromGrant(grant) == null ||
                        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(getRankFromGrant(grant).getUuid()) == null);

        for (Grant grant : getGrants()) {
            if(grant.isStillActive() && isGrantActiveOnScope(grant)) {
                active.add(grant);
            }
        }
        active.sort(Comparator.comparingInt(o -> getRankFromGrant(o).getPriority()));
        return active;
    }

    @Override
    public Grant getCurrentGrant() {
        Grant grant = null;

        for (Grant activeGrant : getActiveGrants()) {
            if (grant == null) {
                grant = activeGrant;
                continue;
            }
            if (getRankFromGrant(activeGrant).getPriority() > getRankFromGrant(grant).getPriority()) {
                grant = activeGrant;
            }
        }

        if (grant == null) {
            applyGrant(CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getDefaultGrant(), null, false);
            for (Grant activeGrant : getActiveGrants()) {
                if (grant == null) {
                    grant = activeGrant;
                    continue;
                }
                if (getRankFromGrant(activeGrant).getPriority() > getRankFromGrant(grant).getPriority()) {
                    grant = activeGrant;
                }
            }
        }

        return grant;
    }

    @Override
    public void applyGrant(Grant grant, UUID executor, boolean shouldGetCurrentGrant) {
        if (getGrants() == null) return;

        getGrants().add(grant);
        if (shouldGetCurrentGrant && getCurrentGrant().getUuid().toString().equalsIgnoreCase(grant.getUuid().toString())) refreshCurrentGrant();

        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getLoggerPopulator()
                .printLog("Successfully applied " + getUsername() + "'s Grant of the " + getRankFromGrant(grant).getName() + " Rank");
    }

    @Override
    public void refreshCurrentGrant() {

    }

    @Override
    public boolean isGrantActiveOnScope(Grant grant) {
        return grant.getScope().stream().anyMatch(s -> (s.equalsIgnoreCase("global") || s.equalsIgnoreCase(Bukkit.getServerName())));
    }

    @Override
    public Rank getRankFromGrant(Grant grant) {
        return CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRank(grant.getRank());
    }
}
