package me.rejomy.skypvp;

import lombok.Getter;
import me.rejomy.skypvp.command.BuyerNMSP;
import me.rejomy.skypvp.command.CreateTeleport;
import me.rejomy.skypvp.command.RTPIsland;
import me.rejomy.skypvp.listener.ConnectionListener;
import me.rejomy.skypvp.listener.EntityDamageListener;
import me.rejomy.skypvp.listener.InteractListener;
import me.rejomy.skypvp.listener.TitanFlyingFeatureListener;
import me.rejomy.skypvp.manager.UserManager;
import me.rejomy.skypvp.util.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class SkyPvP extends JavaPlugin {

    @Getter
    private static SkyPvP instance;

    private UserManager userManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        getCommand("buyernmsp").setExecutor(new BuyerNMSP());
        getCommand("createteleport").setExecutor(new CreateTeleport());
        getCommand("rtpisland").setExecutor(new RTPIsland());

        EconomyManager.init();

        initManagers();
        initListeners();
    }

    private void initListeners() {
        registerListener(new ConnectionListener());
        registerListener(new EntityDamageListener());
        registerListener(new InteractListener());
        registerListener(new TitanFlyingFeatureListener());
    }

    private void initManagers() {
        userManager = new UserManager();
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
