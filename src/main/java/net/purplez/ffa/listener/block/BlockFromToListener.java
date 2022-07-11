package net.purplez.ffa.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToListener implements Listener {

    @EventHandler
    private void handleBlockFromTo(final BlockFromToEvent event) {
        event.setCancelled(true);
    }

}
