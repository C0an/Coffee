package cc.ryaan.coffee.bukkit.util;

import cc.ryaan.coffee.bukkit.CoffeeBukkitPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ColourUtil {

    public static String getLangMessage(String langEntry, String defaultMessage) {
        String message = CoffeeBukkitPlugin.getInstance().getLang().getConfig().getString(langEntry, defaultMessage);
        if(message == null) message = ChatColor.RED + "Failed to find a lang message for \"" + langEntry + "\" - contact an administrator.";
        return translate(message);
    }

    public static String getLangMessage(String langEntry) {
        return getLangMessage(langEntry, null);
    }

    public static void sendLangListMessage(CommandSender commandSender, String langEntry) {
        List<String> messageList = CoffeeBukkitPlugin.getInstance().getLang().getConfig().getStringList(langEntry);
        if(messageList == null || messageList.isEmpty()) commandSender.sendMessage(ChatColor.RED + "Failed to find a lang list message for \"" + langEntry + "\" - contact an administrator.");
        else messageList.forEach(s -> commandSender.sendMessage(translate(s)));
    }

    public static List<String> getLangListMessage(String langEntry) {
        List<String> messageList = CoffeeBukkitPlugin.getInstance().getLang().getConfig().getStringList(langEntry);
        if(messageList == null || messageList.isEmpty()) return Collections.singletonList(ChatColor.RED + "Failed to find a lang list message for \"" + langEntry + "\" - contact an administrator.");
        else return messageList.stream().map(ColourUtil::translate).collect(Collectors.toList());
    }

    public static List<String> translateList(List<String> inputList) {
        return inputList.stream().map(ColourUtil::translate).collect(Collectors.toList());
    }

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
