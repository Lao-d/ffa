package net.purplez.ffa.command;

import net.purplez.ffa.PurplezFFA;
import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     *
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        final Player player = (Player) sender;

        if (args.length == 1) {

            // Set the spawn
            if (args[0].equalsIgnoreCase("spawn")) {
                try {
                    this.setSpawnLocation(player.getLocation());

                    player.sendMessage("Du hast den Spawn gesetzt!");
                } catch (final IOException e) {
                    player.sendMessage("Es ist ein Fehler aufgetreten!");
                }

                return false;
            }

            // Change inventory
            if (args[0].equalsIgnoreCase("changeinventory")) {
                try {
                    this.setChangeInventoryLocation(player.getLocation());

                    player.sendMessage("Du hast den Spawn für den Inventory change gesetzt!");
                } catch (final IOException e) {
                    player.sendMessage("Es ist ein Fehler aufgetreten!");
                }

                return false;
            }

        }

        sender.sendMessage("/setspawn [spawn/changeinventory]");
        return false;
    }

    private void setChangeInventoryLocation(final Location location) throws IOException {
        final File file = new File(PurplezFFA.getInstance().getDataFolder(), "locations.yml");

        if (!file.exists()) file.createNewFile();
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        yamlConfiguration.set("change-inventory.world", location.getWorld().getName());
        yamlConfiguration.set("change-inventory.x", location.getX());
        yamlConfiguration.set("change-inventory.y", location.getY());
        yamlConfiguration.set("change-inventory.z", location.getZ());
        yamlConfiguration.set("change-inventory.yaw", location.getYaw());
        yamlConfiguration.set("change-inventory.pitch", location.getPitch());

        yamlConfiguration.save(file);

        PurplezFFA.getInstance().setChangeInventoryLocation(location);
    }

    private void setSpawnLocation(final Location location) throws IOException {
        final File file = new File(PurplezFFA.getInstance().getDataFolder(), "locations.yml");

        if (!file.exists()) file.createNewFile();
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        yamlConfiguration.set("spawn.world", location.getWorld().getName());
        yamlConfiguration.set("spawn.x", location.getX());
        yamlConfiguration.set("spawn.y", location.getY());
        yamlConfiguration.set("spawn.z", location.getZ());
        yamlConfiguration.set("spawn.yaw", location.getYaw());
        yamlConfiguration.set("spawn.pitch", location.getPitch());

        yamlConfiguration.save(file);

        PurplezFFA.getInstance().setSpawnLocation(location);
    }
}
