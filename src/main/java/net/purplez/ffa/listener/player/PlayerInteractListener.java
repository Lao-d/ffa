package net.purplez.ffa.listener.player;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void handlePlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        final ItemStack itemStack = player.getItemInHand();

        if (itemStack == null) return;
        if (itemStack.getItemMeta() == null) return;
        if (itemStack.getItemMeta().getDisplayName() == null) return;

        switch (itemStack.getItemMeta().getDisplayName()) {

            case "§aHead Apple": {
                player.getInventory().remove(Material.APPLE);
                player.updateInventory();

                player.setMaxHealth(player.getMaxHealth() + 4.0);
                player.setHealth(player.getHealth() + 4.0);
                break;
            }

            case "§aLightning": {
                event.setCancelled(true);

                if (player.hasMetadata("lightning-delay")) break;

                player.getWorld().strikeLightning(player.getEyeLocation());

                player.setMetadata("lightning-delay", new FixedMetadataValue(PurplezFFA.getInstance(), true));

                Bukkit.getScheduler().runTaskLater(
                        PurplezFFA.getInstance(),
                        () -> player.removeMetadata("lightning-delay", PurplezFFA.getInstance()),
                        20 * 5
                );
                break;
            }

        }
    }


    @EventHandler
    public void onHungry(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onReward(PlayerAchievementAwardedEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }




}
