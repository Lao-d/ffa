package net.purplez.ffa.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(final Material material) {
        this(material, (byte) 0, 1);
    }

    public ItemBuilder(final Material material, final byte subId) {
        this(material, subId, 1);
    }

    public ItemBuilder(final Material material, final int amount) {
        this(material, (byte) 0, amount);
    }

    public ItemBuilder(final Material material, final byte subId, final int amount) {
        this.itemStack = new ItemStack(material, amount, subId);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder setName(final String displayName) {
        this.itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment, final byte level) {
        this.itemMeta.addEnchant(enchantment, level, false);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        this.itemMeta.spigot().setUnbreakable(true);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);

        return this.itemStack;
    }

}
