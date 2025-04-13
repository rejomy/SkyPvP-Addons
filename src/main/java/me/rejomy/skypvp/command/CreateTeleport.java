package me.rejomy.skypvp.command;

import me.rejomy.skypvp.SkyPvP;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CreateTeleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || args.length == 0) {
            return false;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        File file = new File(SkyPvP.getInstance().getDataFolder(), "teleport.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        int number = 0;
        Set<String> keys = yaml.getConfigurationSection("") != null
                ? yaml.getConfigurationSection("").getKeys(false)
                : Set.of();

        for (String section : keys) {
            boolean fromCheck = yaml.get(section + ".from") == null && args[0].equalsIgnoreCase("from");
            boolean toCheck = yaml.get(section + ".to") == null && args[0].equalsIgnoreCase("to");

            if (!(fromCheck || toCheck)) {
                number = Integer.parseInt(section) + 1;
            }
        }

        if (args[0].equalsIgnoreCase("to")) {
            yaml.set(number + ".to.x", location.getX());
            yaml.set(number + ".to.y", location.getY());
            yaml.set(number + ".to.z", location.getZ());
        } else {
            yaml.set(number + ".from.x", location.getBlockX());
            yaml.set(number + ".from.y", location.getBlockY());
            yaml.set(number + ".from.z", location.getBlockZ());
        }

        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}