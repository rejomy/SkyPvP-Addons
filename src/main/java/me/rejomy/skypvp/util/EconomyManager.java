package me.rejomy.skypvp.util;

import lombok.experimental.UtilityClass;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

@UtilityClass
public final class EconomyManager {

    private static Economy e;

    public void init() {
        RegisteredServiceProvider<Economy> reg = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (reg != null) {
            e = reg.getProvider();
        }
    }

    public boolean takeMoney(Player p, double price) {
        if (e == null || p == null) {
            return false;
        }

        OfflinePlayer offlinePlayer = p;
        if (e.getBalance(offlinePlayer) < price) {
            return false;
        } else {
            return e.withdrawPlayer(offlinePlayer, price).transactionSuccess();
        }
    }

    public void giveMoney(Player p, double money) {
        if (e == null || p == null) {
            return;
        }

        e.depositPlayer(p, money);
    }

    public double getBalance(Player p) {
        if (e == null || p == null) {
            return 0.0;
        }

        return e.getBalance(p);
    }
}