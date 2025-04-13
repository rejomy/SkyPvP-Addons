package me.rejomy.skypvp.listener;

import me.rejomy.skypvp.SkyPvP;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        // Check for explosion damage in an arena position
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            Player player = (Player) entity;

            event.setDamage(event.getDamage() / 2.0);

            if (SkyPvP.getInstance().getUserManager().getTntOwners().contains(player)) {
                SkyPvP.getInstance().getUserManager().getTntOwners().remove(player);
                event.setDamage(event.getDamage() / 4.0);
            }
        }
    }
}
