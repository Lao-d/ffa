package net.purplez.ffa.listener.player;

import net.purplez.ffa.PurplezFFA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    @EventHandler
    public void handlePlayerRespawn(final PlayerRespawnEvent event) {
        event.setRespawnLocation(PurplezFFA.getInstance().getSpawnLocation());
    }

}
