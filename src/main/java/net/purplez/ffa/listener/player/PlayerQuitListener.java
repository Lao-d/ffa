package net.purplez.ffa.listener.player;

import net.purplez.ffa.PurplezFFA;
import net.purplez.ffa.stats.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void handlePlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final Stats stats = (Stats) player.getMetadata("stats").get(0).value();

        if (stats == null) return;

        PurplezFFA.getInstance().getPurplezFFAConnection().updateStats(stats, () -> {
            // Here you can send the player a message, call an event, no idea. This is just carried out when the player has been entered in the database.
        });
    }

}
