package net.purplez.ffa.database;

import net.purplez.ffa.item.ItemBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventorySortConnection extends DatabaseConnection {

    public InventorySortConnection(final Plugin plugin, final Connection connection, final String database, final String table) {
        super(plugin, connection, database, table);

        this.prepareStatement("CREATE TABLE IF NOT EXISTS _database._table(`uuid` varchar(36), `inv_sort` text);");
    }

    public void updateInventorySort(final UUID uniqueId, final Inventory inventory) {

        this.isPlayerExists(uniqueId, exists -> {
            if (exists) this.update(uniqueId, inventory); else this.createPlayer(uniqueId, inventory);
        });

    }

    public void getInventorySort(final UUID uniqueId, final Consumer<ItemStack[]> consumer) {
        this.runAsynchronously(() -> {
            try {
                final ResultSet resultSet = this.prepareStatement(
                        "SELECT inv_sort FROM _database._table WHERE uuid = ?",
                        uniqueId.toString()
                );

                if (resultSet.next())
                    consumer.accept(this.getInventoryByText(resultSet.getString("inv_sort")).getContents());
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void createPlayer(final UUID uniqueId, final Inventory inventory) {
        this.runAsynchronously(() -> this.updateStatement(
                "INSERT INTO _database._table(`uuid`, `inv_sort`) VALUES (?, ?)",
                uniqueId.toString(),
                this.getInventoryText(inventory)
        ));
    }

    public void update(final UUID uniqueId, final Inventory inventory) {
        this.runAsynchronously(() -> this.updateStatement(
                "UPDATE _database._table SET inv_sort = ? WHERE uuid = ?",
                this.getInventoryText(inventory),
                uniqueId.toString()
        ));
    }

    public void isPlayerExists(final UUID uniqueId, final Consumer<Boolean> consumer) {
        this.runAsynchronously(() -> {
            consumer.accept(this.hasNext("SELECT uuid FROM _database._table WHERE uuid = ?", uniqueId.toString()));
        });
    }

    private Inventory getInventoryByText(final String text) {
        final Inventory inventory = Bukkit.createInventory(null, 9);
        final String[] array = text.split(";");

        for (final String s : array) {
            final String[] s1 = s.split(",");

            inventory.setItem(Integer.parseInt(s1[0]), this.getItemStackById(Integer.parseInt(s1[1])));
        }

        return inventory;
    }


    private String getInventoryText(final Inventory inventory) {
        final StringBuilder stringBuilder = new StringBuilder();
        int i = 0;

        for (final ItemStack itemStack : inventory.getContents()) {
            i++;
            if (itemStack == null) continue;
            if (itemStack.getItemMeta() == null) continue;
            if (itemStack.getItemMeta().getDisplayName() == null) continue;

            stringBuilder.append(i - 1).append(",").append(this.getItemId(itemStack.getItemMeta().getDisplayName())).append(";");
            if (i == 9) break;
        }

        return stringBuilder.toString();
    }


    private ItemStack getItemStackById(final int id) {

        switch (id) {

            case 0: return new ItemBuilder(Material.DIAMOND_SWORD).setName("§aSword").enchant(Enchantment.DAMAGE_ALL, (byte) 2).build();
            case 1: return new ItemBuilder(Material.FISHING_ROD).setName("§aRod").build();
            case 2: return new ItemBuilder(Material.WATER_BUCKET).setName("§aWater").build();
            case 3: return new ItemBuilder(Material.LAVA_BUCKET).setName("§aLava").build();
            case 4: return new ItemBuilder(Material.GOLDEN_APPLE, 1).setName("§aLaolinchen").build();
            case 5: return new ItemBuilder(Material.GOLDEN_APPLE, 4).setName("§aApple").build();
            case 6: return new ItemBuilder(Material.WEB, 16).setName("§aCobweb").build();
            case 7: return new ItemBuilder(Material.COBBLESTONE, 64).setName("§aCobblestone").build();

        }

        return new ItemStack(Material.STONE);
    }

    private int getItemId(final String name) {

        switch (name) {

            case "§aSword": return 0;
            case "§aRod": return 1;
            case "§aWater": return 2;
            case "§aLava": return 3;
            case "§aLaolinchen": return 4;
            case "§aApple": return 5;
            case "§aCobweb": return 6;
            case "§aCobblestone": return 7;

        }

        return -1;
    }
}
