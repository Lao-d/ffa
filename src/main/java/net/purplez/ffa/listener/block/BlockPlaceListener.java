package net.purplez.ffa.listener.block;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void handleBlockPlace(final BlockPlaceEvent event) {
        final Location location = event.getPlayer().getLocation();

        if (location.distance(PurplezFFA.getInstance().getSpawnLocation()) < PurplezFFA.getInstance().getProtectionRadius()) {
            event.setCancelled(true);
            return;
        }
        final Block block = event.getBlock();


        if (block.getType() != Material.WEB && block.getType() != Material.COBBLESTONE) return;

        Bukkit.getScheduler().runTaskLater(PurplezFFA.getInstance(), () -> {
            block.setType(Material.AIR);

            for (final Player player : Bukkit.getOnlinePlayers()) {
                block.getLocation().getWorld().playEffect(block.getLocation(), Effect.EXPLOSION, 1);
                player.playSound(block.getLocation(), Sound.EXPLODE, 10.0f, 10.0f);
            }
        }, 5 * 20);

        if (!(event.getBlock().getType().equals(Material.COBBLESTONE))) {
            event.setCancelled(true);
        }
    }


}
