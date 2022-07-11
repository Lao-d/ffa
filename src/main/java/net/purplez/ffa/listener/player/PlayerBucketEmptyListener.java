package net.purplez.ffa.listener.player;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmptyListener implements Listener {


    @EventHandler
    public void handlePlayerBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Location location = event.getPlayer().getLocation();

        if (location.distance(PurplezFFA.getInstance().getSpawnLocation()) < PurplezFFA.getInstance().getProtectionRadius())
            event.setCancelled(true);

    }

}
