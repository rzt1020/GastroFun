package cn.myrealm.gastrofun.managers.mechanics;


import cn.myrealm.gastrofun.managers.BaseManager;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.BasePlaceableItem;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.Grill;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.MixingBowl;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.Skillet;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rzt1020
 */
public class PlaceableItemManager extends BaseManager {
    private static PlaceableItemManager instance;
    private Map<String, BasePlaceableItem> placeableItems;

    public PlaceableItemManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    public static PlaceableItemManager getInstance() {
        return instance;
    }

    @Override
    protected void onInit() {
        placeableItems = new HashMap<>(5);
        registerPlaceableItem(new Skillet());
        registerPlaceableItem(new Grill());
        registerPlaceableItem(new MixingBowl());
    }

    public void registerPlaceableItem(BasePlaceableItem item) {
        placeableItems.put(item.getPlaceableName(), item);
    }

    public BasePlaceableItem getPlaceableItem(String name) {
        return placeableItems.get(name);
    }

    public BasePlaceableItem getPlaceableItem(Location location) {
        for (BasePlaceableItem item : placeableItems.values()) {
            if (item.isPlacedAt(location)) {
                return item;
            }
        }
        return null;
    }

    @Override
    protected void onDisable() {
        for (BasePlaceableItem placeableItem : placeableItems.values()) {
            placeableItem.hideAll();
        }
    }
}
