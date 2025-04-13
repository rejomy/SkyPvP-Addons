package me.rejomy.skypvp.util;


import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class ItemUtil {

    public void removeItem(Player player, Material type) {
        if (player == null || type == null) return;

        int slot = -1;
        ItemStack handItem = player.getItemInHand();

        if (handItem != null && handItem.getType() == type) {
            slot = player.getInventory().getHeldItemSlot();
        } else {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.getType() == type) {
                    slot = i;
                    break;
                }
            }
        }

        if (slot >= 0) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    player.getInventory().setItem(slot, item);
                } else {
                    player.getInventory().setItem(slot, null);
                }
            }
        }
    }
}