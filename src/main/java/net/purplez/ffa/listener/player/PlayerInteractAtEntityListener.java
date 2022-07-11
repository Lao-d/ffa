package net.purplez.ffa.listener.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractAtEntityListener implements Listener {

    @EventHandler
    public void handlePlayerInteractAtEntity(final PlayerInteractAtEntityEvent event) {
        final Entity entity = event.getRightClicked();

        if (entity.getCustomName() == null) return;
        if (!entity.getCustomName().equals("§aInventarsortierung")) return;
        final Inventory inventory = Bukkit.createInventory(null, 9, "§aInventarsortierung");
        int i = 0;

        for (final ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
            inventory.setItem(i, itemStack);
            i++;

            if (i == 9) break;
        }

        event.getPlayer().openInventory(inventory);
    }

}
