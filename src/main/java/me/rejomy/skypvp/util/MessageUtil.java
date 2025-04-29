package me.rejomy.skypvp.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@UtilityClass
public class MessageUtil {

    public void sendMessage(Player player, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
    }
}
