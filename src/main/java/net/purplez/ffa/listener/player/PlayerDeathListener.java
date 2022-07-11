package net.purplez.ffa.listener.player;

import net.purplez.ffa.stats.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void handlePlayerDeath(final PlayerDeathEvent event) {
        final Player
                player = event.getEntity(),
                killer = player.getKiller();

        // Add a death for the player
        final Stats playerStats = (Stats) player.getMetadata("stats").get(0).value();

        player.spigot().respawn();

        event.setKeepInventory(true);

        playerStats.addDeaths(1);
        if (killer == null) return;
        // Add a kill for the killer
        final Stats killerStats = (Stats) killer.getMetadata("stats").get(0).value();

        killerStats.addKills(1);
    }

}
