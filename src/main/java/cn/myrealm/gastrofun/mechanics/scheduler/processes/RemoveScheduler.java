package cn.myrealm.gastrofun.mechanics.scheduler.processes;


import cn.myrealm.gastrofun.mechanics.items.items.BasePlaceableItem;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author rzt1020
 */
public class RemoveScheduler extends BaseScheduler {
    private boolean remove = true;
    private final BasePlaceableItem placeableItem;
    private final Location location;
    public RemoveScheduler(JavaPlugin plugin, long period, long endTicks, BasePlaceableItem placeableItem, Location location) {
        super(plugin, period, endTicks);
        this.placeableItem = placeableItem;
        this.location = location.clone();
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    @Override
    public void end() {
        if (remove) {
            placeableItem.remove(location);
        }
    }
}
