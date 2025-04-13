package me.rejomy.skypvp.listener;

import me.rejomy.skypvp.SkyPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Remove player if exists for prevent memory leaks.
        SkyPvP.getInstance().getUserManager().getTntOwners().remove(player);
    }
}
