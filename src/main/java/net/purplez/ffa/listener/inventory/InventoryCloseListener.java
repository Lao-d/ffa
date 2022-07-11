package net.purplez.ffa.listener.inventory;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void handleInventoryClose(final InventoryCloseEvent event) {
        final Inventory inventory = event.getInventory();

        if (!inventory.getTitle().equals("§aInventarsortierung")) return;
        final Player player = (Player) event.getPlayer();

        player.getInventory().clear();
        player.getInventory().setContents(inventory.getContents());

        Bukkit.getScheduler().runTaskLater(PurplezFFA.getInstance(), player::updateInventory, 1);
        PurplezFFA.getInstance().getInventorySortConnection().updateInventorySort(player.getUniqueId(), inventory);
    }

}
