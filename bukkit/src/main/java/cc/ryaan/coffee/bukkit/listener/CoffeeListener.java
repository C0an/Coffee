package cc.ryaan.coffee.bukkit.listener;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.profile.ProfileBukkit;
import cc.ryaan.coffee.profile.obj.LoadType;
import cc.ryaan.coffee.util.EncryptionHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CoffeeListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String loggedInIP = EncryptionHandler.encryptUsingKey(event.getAddress().getHostAddress());
        AtomicReference<ProfileBukkit> atomicReference = new AtomicReference<>();

        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().loadProfile(uuid.toString(), profileFound -> atomicReference.set((ProfileBukkit) profileFound), false, LoadType.UUID);

        ProfileBukkit profile = atomicReference.get();
        if(profile == null) (profile = new ProfileBukkit(uuid, event.getName())).applyGrant(CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getDefaultGrant(), null, false);

        profile.setCurrentIPAddress(loggedInIP);
        profile.getPreviousIPAddresses().add(loggedInIP);

        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().addProfile(uuid, profile);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProfileBukkit profile = (ProfileBukkit) CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().getProfileByUUID(player.getUniqueId());

        if(profile.getFirstJoined() == 0) profile.setFirstJoined(System.currentTimeMillis());
        profile.setLastJoined(System.currentTimeMillis());
        profile.setUsername(player.getName());

        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().saveProfile(profile, ignored -> {}, true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ProfileBukkit profile = (ProfileBukkit) CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().getProfileByUUID(event.getPlayer().getUniqueId());
        if (profile != null) {
            profile.setLastQuit(System.currentTimeMillis());
            profile.setConnectedServer(Bukkit.getServerName());
            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().saveProfile(profile, ignored -> {}, true);
            CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getProfileHandler().getProfiles().remove(event.getPlayer().getUniqueId());
        }
    }

}
