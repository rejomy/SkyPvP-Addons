package me.rejomy.skypvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Fireball)) {
            return;
        }

        Location location = event.getLocation();
        double radius = 5.0;
        double heightForce = 0.75;
        double radiusForce = 1.5;

        for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (nearbyEntity instanceof Player) {
                LivingEntity player = (LivingEntity) nearbyEntity;
                this.pushAway(player, location, heightForce, radiusForce, event);
            }
        }
    }

    public void pushAway(LivingEntity player, Location location, double heightForce, double radiusForce, EntityExplodeEvent event) {
        Location playerLocation = player.getLocation();
        double damage = 3.0;
        double distance = event.getYield() * 16.0f;
        distance *= 1.0;
        double hf1 = Math.max(-4.0, Math.min(4.0, heightForce));
        double rf1 = Math.max(-4.0, Math.min(4.0, -1 * radiusForce));

        player.setVelocity(location.toVector().subtract(playerLocation.toVector()).normalize().multiply(rf1).setY(hf1));

        EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, distance - playerLocation.distance(location) + damage);
        Bukkit.getPluginManager().callEvent(damageEvent);

        if (!damageEvent.isCancelled()) {
            player.damage(damageEvent.getFinalDamage());
        }
    }
}