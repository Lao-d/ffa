package net.purplez.ffa.item;

import com.google.common.collect.Maps;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemStorage {

    private final Map<String, ItemStack[]> itemStackArray;
    private final Map<String, Inventory> inventories;

    public ItemStorage() {
        this.itemStackArray = Maps.newConcurrentMap();
        this.inventories = Maps.newConcurrentMap();
    }

    /**
     * Cache all items and inventories
     */
    public void load() {

        // default player inventory
        {
            final Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);

            inventory.setItem(0, new ItemBuilder(Material.DIAMOND_SWORD).setName("§aSword").enchant(Enchantment.DAMAGE_ALL, (byte) 2).build());
            inventory.setItem(1, new ItemBuilder(Material.FISHING_ROD).setName("§aRod").build());
            inventory.setItem(2, new ItemBuilder(Material.WATER_BUCKET).setName("§aWater").build());
            inventory.setItem(3, new ItemBuilder(Material.LAVA_BUCKET).setName("§aLava").build());
            inventory.setItem(4, new ItemBuilder(Material.GOLDEN_APPLE, 1).setName("§aLaolinchen").build());
            inventory.setItem(5, new ItemBuilder(Material.GOLDEN_APPLE, 4).setName("§aApple").build());
            inventory.setItem(6, new ItemBuilder(Material.WEB, 16).setName("§aCobweb").build());
            inventory.setItem(7, new ItemBuilder(Material.COBBLESTONE, 64).setName("§aCobblestone").build());

            this.inventories.put("default-inventory", inventory);
        }

        // 5 kills inventory
        {
            final Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);

            inventory.setContents(this.getInventory("default-inventory").getContents());

            inventory.addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).setName("§aGolden Apples").build());
            inventory.addItem(new ItemBuilder(Material.APPLE).setName("§aHead Apple").build());

            this.inventories.put("5-kills-inventory", inventory);
        }

        // 10 kills inventory
        {
            final Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);

            inventory.setContents(this.getInventory("default-inventory").getContents());

            inventory.addItem(new ItemBuilder(Material.GOLDEN_APPLE, 5).setName("§aGolden Apples").build());
            inventory.addItem(new ItemBuilder(Material.APPLE, 2).setName("§aHead Apple").build());
            inventory.addItem(new ItemBuilder(Material.BLAZE_ROD).setName("§aLightning").build());

            this.inventories.put("10-kills-inventory", inventory);
        }

        // default armor
        {
            this.itemStackArray.put("armor", new ItemStack [] {
                    new ItemBuilder(Material.DIAMOND_BOOTS)
                            .setName("§aBoots")
                            .enchant(Enchantment.PROTECTION_PROJECTILE, (byte) 2)
                            .setUnbreakable()
                            .build(),
                    new ItemBuilder(Material.IRON_LEGGINGS)
                            .setName("§aLeggins")
                            .enchant(Enchantment.PROTECTION_PROJECTILE, (byte) 2)
                            .setUnbreakable()
                            .build(),
                    new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                            .setName("§aChestplate")
                            .enchant(Enchantment.PROTECTION_PROJECTILE, (byte) 2)
                            .setUnbreakable()
                            .build(),
                    new ItemBuilder(Material.IRON_HELMET)
                            .setName("§aHelmet")
                            .enchant(Enchantment.PROTECTION_PROJECTILE, (byte) 2)
                            .setUnbreakable()
                            .build()
            });

        }
    }

    public Inventory getInventory(final String key) {
        synchronized (this.inventories) {
            return this.inventories.getOrDefault(key, Bukkit.createInventory(null, 9, "§4Not found!"));
        }
    }

    public ItemStack[] getArray(final String key) {
        synchronized (this.itemStackArray) {
            return this.itemStackArray.getOrDefault(key, new ItemStack[] {});
        }
    }

}
