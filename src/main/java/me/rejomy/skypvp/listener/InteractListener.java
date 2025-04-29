package me.rejomy.skypvp.listener;

import me.rejomy.skypvp.SkyPvP;
import me.rejomy.skypvp.util.ItemUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Set;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = player.getInventory().getItemInMainHand();

            // NETHER_STAR logic
            if (item.getType() == Material.NETHER_STAR) {
                TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                tnt.setFuseTicks(-1);
                SkyPvP.getInstance().getUserManager().getTntOwners().add(player);

                if (player.getGameMode() != GameMode.CREATIVE) {
                    ItemUtil.removeItem(player, Material.NETHER_STAR);
                }
                
                return;
            }

            // Soup / Stew healing logic
            Material type = item.getType();
            if ((type == Material.MUSHROOM_STEW || type == Material.RABBIT_STEW) && player.getHealth() < player.getHealthScale()) {
                ItemMeta itemMeta = item.getItemMeta().clone();
                item = new ItemStack(Material.BOWL, item.getAmount());
                item.setItemMeta(itemMeta);
                player.getInventory().setItemInMainHand(item);
                double healAmount = 4.0;
                double newHealth = Math.min(player.getHealth() + healAmount, player.getHealthScale());
                player.setHealth(newHealth);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
                event.setCancelled(true);
            }

        } else if (action == Action.PHYSICAL) {
            // Pressure plate teleport logic
            if (event.getClickedBlock() == null) return;

            Location location = event.getClickedBlock().getLocation();
            File file = new File(SkyPvP.getInstance().getDataFolder(), "teleport.yml");
            if (!file.exists()) return;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            Set<String> sections = yaml.getKeys(false);
            for (String section : sections) {
                String prefix = section + ".from";
                int fx = yaml.getInt(prefix + ".x");
                int fy = yaml.getInt(prefix + ".y");
                int fz = yaml.getInt(prefix + ".z");

                if (location.getBlockX() == fx && location.getBlockY() == fy && location.getBlockZ() == fz) {
                    String to = section + ".to";
                    double tx = yaml.getDouble(to + ".x");
                    double ty = yaml.getDouble(to + ".y");
                    double tz = yaml.getDouble(to + ".z");

                    player.teleport(new Location(player.getWorld(), tx, ty, tz));
                    return;
                }
            }
        }
    }
}
