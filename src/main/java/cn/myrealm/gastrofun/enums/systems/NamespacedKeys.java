package cn.myrealm.gastrofun.enums.systems;

import cn.myrealm.gastrofun.GastroFun;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author rzt1020
 */

public enum NamespacedKeys {
    // food namespaced keys
    FOOD_NAME("food_name"),
    FOOD_POINT("food_point"),
    SATURATION("saturation"),
    // placeable food namespaced keys
    PLACEABLE_FOOD_NAME("placeable_food_name"),
    PLACEABLE_FOOD_ITEM_STACK("placeable_food_item_stack.{0}"),
    // placeable item namespaced keys
    PLACEABLE_NAME("placeable_name"),
    // chunk placeable item namespaced keys
    PLACEABLE_ITEM_LIST("placeable_item_list"),
    PLACEABLE_ITEM_NAME("placeable_item_name.{0}"),
    PLACEABLE_ITEM_LOCATION("placeable_item_location.{0}"),
    PLACEABLE_ITEM_ROTATION("placeable_item_rotation.{0}"),
    PLACEABLE_ITEM_STATE("placeable_item_state.{0}"),
    // misc
    ITEM_STACK("item_stack.{0}");

    private final String key;
    NamespacedKeys(String key) {
        this.key = key;
    }

    public <P, C> boolean has(PersistentDataHolder holder, PersistentDataType<P, C> type, String... args) {
        return has(holder.getPersistentDataContainer(), type, args);
    }
    public <P, C> boolean has(PersistentDataContainer container, PersistentDataType<P, C> type, String... args) {
        NamespacedKey key = getNamespacedKey(args);
        return container.has(key, type);
    }

    public <P, C> Object get(PersistentDataHolder holder, PersistentDataType<P, C> type, String... args) {
        return get(holder.getPersistentDataContainer(), type, args);
    }
    public <P, C> Object get(PersistentDataContainer container, PersistentDataType<P, C> type, String... args) {
        NamespacedKey key = getNamespacedKey(args);
        return container.get(key, type);
    }

    public <P, C> void set(PersistentDataHolder holder, PersistentDataType<P, C> type, C value, String... args) {
        set(holder.getPersistentDataContainer(), type, value, args);
    }
    public <P, C> void set(PersistentDataContainer container, PersistentDataType<P, C> type, C value, String... args) {
        NamespacedKey key = getNamespacedKey(args);
        container.set(key, type, value);
    }

    public NamespacedKey getNamespacedKey(String... args) {
        String key = this.key;
        for (int i = 0; i < args.length; i++) {
            key = key.replace("{" + i + "}", args[i]);
        }
        return new NamespacedKey(GastroFun.plugin, key);
    }

}
