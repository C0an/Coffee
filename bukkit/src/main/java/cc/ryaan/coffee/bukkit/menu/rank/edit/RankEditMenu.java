package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RankEditMenu extends Menu {

    private final Rank rank;

    public RankEditMenu(Rank rank) {
        super(rank.getColouredName());
        this.rank = rank;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        return buttonMap;
    }
}
