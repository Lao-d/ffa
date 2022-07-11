package net.purplez.ffa.listener.entity;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void handleEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Location location = event.getEntity().getLocation();

        if (location.distance(PurplezFFA.getInstance().getSpawnLocation()) < PurplezFFA.getInstance().getProtectionRadius()) {
            event.setCancelled(true);
            return;
        }
        final Location location1 = event.getDamager().getLocation();

        if (location1.distance(PurplezFFA.getInstance().getSpawnLocation()) < PurplezFFA.getInstance().getProtectionRadius()) {
            event.setCancelled(true);
            return;
        }
    }

}
