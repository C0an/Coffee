package cc.ryaan.coffee.bukkit.menu.rank.edit;

import cc.ryaan.coffee.bukkit.menu.rank.edit.button.RankSubmenuButton;
import cc.ryaan.coffee.bukkit.menu.rank.manage.RankManagementMenu;
import cc.ryaan.coffee.rank.Rank;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.protocol.plib.menu.Button;
import rip.protocol.plib.menu.Menu;
import rip.protocol.plib.menu.buttons.BackButton;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RankEditMenu extends Menu {

    private final Rank rank;

    public RankEditMenu(Rank rank) {
        super(rank.getColouredName());
        this.rank = rank;
        setPlaceholder(true);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();

        buttonMap.put(0, new BackButton(new RankManagementMenu()));

        buttonMap.put(11, new RankSubmenuButton(rank, new RankNameSettingsMenu(rank), Material.NAME_TAG, "Name Settings"));
        buttonMap.put(13, new RankSubmenuButton(rank, new RankChatSettingsMenu(rank), Material.IRON_INGOT, "Chat Settings"));
        buttonMap.put(15, new RankSubmenuButton(rank, new RankOtherSettingsMenu(rank), Material.TNT, "Other Settings"));
        buttonMap.put(18, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)15, ""));

        return buttonMap;
    }
}
