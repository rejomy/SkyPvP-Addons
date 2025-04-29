package me.rejomy.skypvp.listener;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import lombok.Setter;
import me.rejomy.skypvp.SkyPvP;
import me.rejomy.skypvp.util.MessageUtil;
import me.rejomy.skypvp.util.RussianTimeUnitFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

// The idea is that in server called dexland, exactly in kitpvp there is kit called titan that have
// book and when you clicked to the book, it activates fly to you for ten seconds.
public class TitanFlyingFeatureListener implements Listener {

    HashMap<Player, Long> players = new HashMap<>();

    @EventHandler
    public void onQuit(PlayerJoinEvent event) {
        players.remove(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        boolean rightClick = event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR;
        int lastActivateWasSecondsAgo = (int) ((System.currentTimeMillis() - players.getOrDefault(player, 0L)) / 1000);
        boolean doesNotHasCooldown = lastActivateWasSecondsAgo > 60;

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                LegacyComponentSerializer.legacySection().serialize(item.getItemMeta().displayName()).equals("§cПолёт!") &&
                rightClick) {
            if (doesNotHasCooldown) {
                MessageUtil.sendMessage(player, "&c> &fСпособность была использована!");
                player.setAllowFlight(true);
                players.put(player, System.currentTimeMillis());
                var flyingTask = new FlyingTask();
                flyingTask.setPlayer(player);
                flyingTask.setTaskId(
                        Bukkit.getScheduler().runTaskTimer(SkyPvP.getInstance(), flyingTask, 20, 20).getTaskId()
                );
            } else {
                int timeLeft = 60 - lastActivateWasSecondsAgo;
                MessageUtil.sendMessage(player, "&c> &fСпособность будет доступна через &a" + timeLeft + " &fсекунд" +
                        RussianTimeUnitFormat.addEndingToSeconds(timeLeft, "у"));
            }
        }
    }

    @Setter
    private static class FlyingTask implements Runnable {

        private int taskId;
        private Player player;
        private int executionCount;

        @Override
        public void run() {
            executionCount++;

            // If 10 seconds elapsed since enabled, we are stop the task.
            if (executionCount == 10) {
                Bukkit.getScheduler().cancelTask(taskId);
                player.setAllowFlight(false);
                player.setFlying(false);
            } else {
                int timeLeft = 10 - executionCount;
                String message = "&c> &fОсталось &a" + timeLeft + " &fсекунд" +
                        RussianTimeUnitFormat.addEndingToSeconds(timeLeft, "а") +
                        " до окончания полёта.";
                MessageUtil.sendMessage(player, message);
            }
        }
    }
}
