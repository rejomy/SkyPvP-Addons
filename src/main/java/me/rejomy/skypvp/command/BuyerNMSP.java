package me.rejomy.skypvp.command;

import me.rejomy.skypvp.util.EconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public final class BuyerNMSP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("Команда может быть выполнена только с консоли!");
            return false;
        }

        if (args.length < 4) {
            sender.sendMessage("Недостаточно аргументов " + args.length + " < 4 НИК ЦЕНА ПРЕДМЕТ МАКС_КОЛ-ВО");
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            return true;
        }

        int price;
        int maxAmount;
        try {
            price = Integer.parseInt(args[1]);
            maxAmount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Неверный формат числа в аргументах.");
            return false;
        }

        String material = args[2];
        double balance = EconomyManager.getBalance(player);
        double amount = Math.floor(balance / price);

        if (amount > maxAmount) {
            amount = maxAmount;
        }

        if (amount < 1) {
            player.sendMessage("§cУ вас недостаточно средств!");
        } else {
            EconomyManager.takeMoney(player, amount * price);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + args[0] + " " + material + " " + (int) amount);
            player.sendMessage("§7» §fПокупка прошла успешно!");
        }

        return false;
    }
}
