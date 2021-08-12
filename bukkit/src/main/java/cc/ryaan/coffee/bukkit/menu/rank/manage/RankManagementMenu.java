package cc.ryaan.coffee.bukkit.menu.rank.manage;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import cc.ryaan.coffee.bukkit.menu.rank.manage.button.CreateRankButton;
import cc.ryaan.coffee.bukkit.menu.rank.manage.button.RankButton;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class RankManagementMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Ranks";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        CoffeeBukkitPlugin.getInstance().getCoffeeBukkit().getRankHandler().getRanks().forEach(rank -> buttonMap.put(buttonMap.size(), new RankButton(rank)));
        return buttonMap;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        buttonMap.put(4, new CreateRankButton(player));
        return buttonMap;
    }
}
