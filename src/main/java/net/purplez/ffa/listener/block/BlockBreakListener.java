package net.purplez.ffa.listener.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {


    @EventHandler
    public void handleBlockBreak(final BlockBreakEvent event) {
        event.setCancelled(true);
    }

}
