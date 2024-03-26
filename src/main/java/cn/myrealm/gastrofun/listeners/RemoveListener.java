package cn.myrealm.gastrofun.listeners;


import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.BasePlaceableItem;
import cn.myrealm.gastrofun.mechanics.scheduler.processes.RemoveScheduler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * @author rzt1020
 */
public class RemoveListener extends BaseListener {

    public RemoveListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) ) {
            return;
        }
        ItemStack itemStack = event.getItem();
        Location location = Objects.requireNonNull(event.getClickedBlock()).getLocation();
        BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(location);
        if (Objects.isNull(placeableItem)) {
            return;
        }
        event.setCancelled(true);
        if (!placeableItem.removable(itemStack)) {
            return;
        }
        placeableItem.tryRemove(location);
        RemoveScheduler removeScheduler = new RemoveScheduler(plugin, 1L, 20L, placeableItem, location);
        removeScheduler.play(0);
        new DigListener(plugin, removeScheduler, placeableItem, location).registerProtocolListener();
    }

}
