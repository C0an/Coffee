package cc.ryaan.coffee.bukkit.menu.rank.create;

import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class RankCreateMenu extends Menu {

    public RankCreateMenu() {
        super("Create a Rank");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        return buttonMap;
    }

}
