package net.purplez.ffa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.purplez.ffa.command.SetSpawnCommand;
import net.purplez.ffa.database.PurplezFFAConnection;
import net.purplez.ffa.database.InventorySortConnection;
import net.purplez.ffa.database.MySQLConnectorClient;
import net.purplez.ffa.item.ItemStorage;
import net.purplez.ffa.listener.block.BlockBreakListener;
import net.purplez.ffa.listener.block.BlockFromToListener;
import net.purplez.ffa.listener.block.BlockPlaceListener;
import net.purplez.ffa.listener.entity.EntityDamageByEntityListener;
import net.purplez.ffa.listener.inventory.InventoryCloseListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.Setter;
import net.purplez.ffa.listener.player.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PurplezFFA extends JavaPlugin {

    @Getter private static PurplezFFA instance;

    @Getter private final ExecutorService executorService;
    @Getter private final ItemStorage itemStorage;
    @Getter private final Gson gson;

    @Getter @Setter private Location spawnLocation, changeInventoryLocation;
    @Getter private InventorySortConnection inventorySortConnection;
    @Getter private PurplezFFAConnection purplezFFAConnection;
    @Getter private double protectionRadius;
    private MySQLConnectorClient client;

    public PurplezFFA() {
        this.executorService = Executors.newCachedThreadPool();
        this.itemStorage = new ItemStorage();
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        // Establish a connection to the database
        this.connectDatabase();

        // Cache the items and inventories
        this.itemStorage.load();

        // Load and cache the spawn location
        this.loadSpawnLocation();

        // Register all commands and listeners
        this.registerListeners();
        this.registerCommands();

        // Spawn the change inventory armor stand
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, this::spawnChangeInventory, 20L);
    }

    @Override
    public void onDisable() {
        this.executorService.shutdown();
        this.client.close();
    }

    /**
     * Register all commands
     */
    private void registerCommands() {

        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());

    }

    /**
     * Register all listeners
     */
    private void registerListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();

        // Block listeners
        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new BlockFromToListener(), this);
        pluginManager.registerEvents(new BlockPlaceListener(), this);

        // Entity listeners
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);

        // Inventory listeners
        pluginManager.registerEvents(new InventoryCloseListener(), this);

        // Player listeners
        pluginManager.registerEvents(new PlayerBucketEmptyListener(), this);
        pluginManager.registerEvents(new PlayerDeathListener(), this);
        pluginManager.registerEvents(new PlayerInteractAtEntityListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new PlayerJoinListener(this.itemStorage), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);
        pluginManager.registerEvents(new PlayerRespawnListener(), this);
    }

    /**
     * Establish a connection to the database.
     */
    private void connectDatabase() {
        final File file = new File(PurplezFFA.getInstance().getDataFolder(), "config.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();

            try (final InputStream inputStream = this.getResource(file.getName())) {
                if (inputStream != null) Files.copy(inputStream, file.toPath());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection section = yamlConfiguration.getConfigurationSection("database");

        this.protectionRadius = yamlConfiguration.getDouble("protection-radius");

        this.client = new MySQLConnectorClient(section );
        this.purplezFFAConnection = new PurplezFFAConnection(
                this,
                this.client.getMySQLInstance(),
                section.getString("database"),
                "builduhc_stats"
        );
        this.inventorySortConnection = new InventorySortConnection(
                this,
                this.client.getMySQLInstance(),
                section.getString("database"),
                "builduhc_inventory"
        );

        this.getLogger().info("The connection to the database has been established.");
    }

    /**
     * Load and cache the spawn location
     */
    private void loadSpawnLocation() {
        final File file = new File(this.getDataFolder(), "locations.yml");
        final Location defaultLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0, 0.0f, 0.0f);

        if (!file.exists()) {
            this.spawnLocation = defaultLocation;
            return;
        }

        // Spawn location
        {
            final ConfigurationSection section = YamlConfiguration.loadConfiguration(file).getConfigurationSection("spawn");

            if (section == null) {
                this.spawnLocation = defaultLocation;
                return;
            }
            final String world = section.getString("world");

            if (world == null) {
                this.spawnLocation = defaultLocation;
                return;
            }

            this.spawnLocation = new Location(
                    Bukkit.getWorld(world),
                    section.getDouble("x"),
                    section.getDouble("y"),
                    section.getDouble("z"),
                    (float) section.getDouble("yaw"),
                    (float) section.getDouble("pitch")
            );
        }

        // Change Inventory Location
        {
            final ConfigurationSection section = YamlConfiguration.loadConfiguration(file).getConfigurationSection("change-inventory");

            if (section == null) return;
            final String world = section.getString("world");

            if (world == null) return;

            this.changeInventoryLocation = new Location(
                    Bukkit.getWorld(world),
                    section.getDouble("x"),
                    section.getDouble("y"),
                    section.getDouble("z"),
                    (float) section.getDouble("yaw"),
                    (float) section.getDouble("pitch")
            );
        }
    }

    /**
     * Spawn the change inventory armor stand
     */
    private void spawnChangeInventory() {
        if (this.changeInventoryLocation == null) return;
        final ArmorStand armorStand = (ArmorStand) this.changeInventoryLocation.getWorld().spawnEntity(
                this.changeInventoryLocation,
                EntityType.ARMOR_STAND
        );

        armorStand.setCustomName("§aInventarsortierung");
        armorStand.setGravity(false);
    }
}
