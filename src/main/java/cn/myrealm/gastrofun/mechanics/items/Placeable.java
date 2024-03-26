package cn.myrealm.gastrofun.mechanics.items;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

/**
 * @author rzt1020
 */
public interface Placeable {
    /**
     * to place item.
     * @param location the location to place
     * @param rotation the rotation of the item
     * @param state the state of the item
     * @param itemStack the item to place
      */

    void place(Location location, Quaternionf rotation, int state, ItemStack itemStack);

    /**
     * to remove item.
     * @param location the location to remove
     */
    void remove(Location location);

    /**
     * to display the item.
     * @param location the location to display
     * @param rotation the rotation of the item
     * @param state the state of the item
     */
    void display(Location location, Quaternionf rotation, int state);

    /**
     * to hide the item.
     * @param location the location to hide
     */
    void hide(Location location);
}
