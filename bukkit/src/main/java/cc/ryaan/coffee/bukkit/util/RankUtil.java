package cc.ryaan.coffee.bukkit.util;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class RankUtil {

    public static DyeColor getWoolColour(char str) {
        ChatColor color = ChatColor.getByChar(str);
        switch (color) {
            case DARK_BLUE:
            case BLUE: {
                return DyeColor.BLUE;
            }
            case DARK_GREEN: {
                return DyeColor.GREEN;
            }
            case DARK_AQUA:
            case AQUA: {
                return DyeColor.CYAN;
            }
            case DARK_RED:
            case RED: {
                return DyeColor.RED;
            }
            case DARK_PURPLE: {
                return DyeColor.PURPLE;
            }
            case GOLD: {
                return DyeColor.ORANGE;
            }
            case GRAY:
            case DARK_GRAY: {
                return DyeColor.GRAY;
            }
            case GREEN: {
                return DyeColor.LIME;
            }
            case LIGHT_PURPLE: {
                return DyeColor.PINK;
            }
            case YELLOW: {
                return DyeColor.YELLOW;
            }
            case WHITE: {
                return DyeColor.WHITE;
            }
            default: {
                return DyeColor.BLACK;
            }
        }
    }

}
