package cn.myrealm.gastrofun.mechanics.items;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author rzt1020
 */
public interface Triggerable {

    /**
     * call when the item is clicked
     * @param player the player who clicked
     * @param itemStack the item player hold
     * @param location the location of the click
     * @return true if the item is consumed
     */
    boolean trigger(Player player, ItemStack itemStack, Location location);
}
