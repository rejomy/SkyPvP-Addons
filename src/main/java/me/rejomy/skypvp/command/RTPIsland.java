package me.rejomy.skypvp.command;

import me.rejomy.skypvp.SkyPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RTPIsland implements CommandExecutor {

    private final File file;
    private YamlConfiguration locations;
    private final ArrayList<Location> locks = new ArrayList<>();

    public RTPIsland() {
        this.file = new File(SkyPvP.getInstance().getDataFolder(), "rtp.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.locations = YamlConfiguration.loadConfiguration(file);

        for (String loc : locations.getStringList("list")) {
            String[] split = loc.split(" ");
            if (split.length < 6) continue;

            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);
            double z = Double.parseDouble(split[2]);
            float yaw = Float.parseFloat(split[3]);
            float pitch = Float.parseFloat(split[4]);
            String worldName = split[5];

            Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            locks.add(location);
        }
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getLocations() {
        return locations;
    }

    public void setLocations(YamlConfiguration locations) {
        this.locations = locations;
    }

    public ArrayList<Location> getLocks() {
        return locks;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (locks.isEmpty()) {
                player.sendMessage("No RTP locations available.");
                return false;
            }

            player.teleport(locks.get(new Random().nextInt(locks.size())));
            return true;
        }

        if (!sender.hasPermission("set")) {
            sender.sendMessage("No permission!");
            return false;
        }

        Location loc = player.getLocation();
        String serialized = loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch() + " " + loc.getWorld().getName();

        List<String> list = locations.getStringList("list");
        list.add(serialized);
        locations.set("list", list);

        try {
            locations.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage(list.size() == 1 ? "One location set." : "Another location set.");
        return true;
    }
}
