package net.purplez.ffa.listener.player;

import net.purplez.ffa.PurplezFFA;
import net.purplez.ffa.item.ItemStorage;
import net.purplez.ffa.listener.scoreboard.ScoreboardSet;
import net.purplez.ffa.stats.Stats;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final ItemStorage itemStorage;

    @EventHandler
    public void handlePlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();



        // Set the inventory on 0
        player.getInventory().clear();


        // Set the stats in the meta data
        player.setMetadata("stats", new FixedMetadataValue(PurplezFFA.getInstance(), new Stats(player.getUniqueId())));

        // Teleport to spawn location

        // Set the default health
        player.setMaxHealth(20.0);
        player.setHealth(20.0);

        // Set the inventory items
        player.getInventory().setContents(this.itemStorage.getInventory("default-inventory").getContents());
        player.getInventory().setArmorContents(this.itemStorage.getArray("armor"));

        // Set the inventory sort
        PurplezFFA.getInstance().getInventorySortConnection().getInventorySort(
                player.getUniqueId(),
                itemStacks -> player.getInventory().setContents(itemStacks)
        );
        ScoreboardSet.setBoard(player);

    }

}
