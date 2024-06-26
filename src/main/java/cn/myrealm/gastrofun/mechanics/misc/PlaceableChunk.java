package cn.myrealm.gastrofun.mechanics.misc;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.items.items.BasePlaceableItem;
import cn.myrealm.gastrofun.mechanics.items.tiles.BasePlaceableItemTile;
import cn.myrealm.gastrofun.mechanics.persistent.ItemStackTagType;
import cn.myrealm.gastrofun.mechanics.persistent.LocationTagType;
import cn.myrealm.gastrofun.mechanics.persistent.QuaternionTagType;
import cn.myrealm.gastrofun.utils.BasicUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class PlaceableChunk {
    private final Chunk chunk;
    private int[] placeableItemsEntityIds;
    private final Map<Integer, String> placeableNameMap = new HashMap<>();
    private final Map<Integer, Location> placeableItemLocationMap = new HashMap<>();
    private final Map<Integer, Quaternionf> placeableItemRotationMap = new HashMap<>();
    private final Map<Integer, Integer> placeableItemStateMap = new HashMap<>();
    private final static ItemStackTagType ITEM_STACK_TAG_TYPE = new ItemStackTagType();



    public PlaceableChunk(String chunkName) {
        String[] args = chunkName.split(",");
        this.chunk = Objects.requireNonNull(Bukkit.getWorld(args[0])).getChunkAt(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        placeableItemsEntityIds = new int[0];
        if (NamespacedKeys.PLACEABLE_ITEM_LIST.has(chunk, PersistentDataType.INTEGER_ARRAY)) {
            placeableItemsEntityIds = (int[]) NamespacedKeys.PLACEABLE_ITEM_LIST.get(chunk, PersistentDataType.INTEGER_ARRAY);
        }
        for (int entityId : placeableItemsEntityIds) {
            String placeableName = (String) NamespacedKeys.PLACEABLE_ITEM_NAME.get(chunk, PersistentDataType.STRING, String.valueOf(entityId));
            Location location = (Location) NamespacedKeys.PLACEABLE_ITEM_LOCATION.get(chunk, new LocationTagType(), String.valueOf(entityId));
            Quaternionf rotation = (Quaternionf) NamespacedKeys.PLACEABLE_ITEM_ROTATION.get(chunk, new QuaternionTagType(), String.valueOf(entityId));
            Integer state = (Integer) NamespacedKeys.PLACEABLE_ITEM_STATE.get(chunk, PersistentDataType.INTEGER, String.valueOf(entityId));
            placeableNameMap.put(entityId, placeableName);
            placeableItemLocationMap.put(entityId, location);
            placeableItemRotationMap.put(entityId, rotation);
            placeableItemStateMap.put(entityId, state);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                display();
            }
        }.runTaskLater(GastroFun.plugin, 1L);

    }

    public void display() {
        for (int entityId : placeableItemsEntityIds) {
            BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(placeableNameMap.get(entityId));
            placeableItem.display(placeableItemLocationMap.get(entityId), placeableItemRotationMap.get(entityId), placeableItemStateMap.get(entityId), entityId);
        }
    }

    public boolean unloadChunk() {
        for (int entityId : placeableItemsEntityIds) {
            BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(placeableNameMap.get(entityId));
            BasePlaceableItemTile placeableItemTile = placeableItem.getTile(placeableItemLocationMap.get(entityId));
            if (placeableItemTile.isFunctioning()) {
                return false;
            }
        }
        forceUnloadChunk();
        return true;
    }

    public void forceUnloadChunk() {
        for (int entityId : placeableItemsEntityIds) {
            NamespacedKeys.PLACEABLE_ITEM_NAME.set(chunk, PersistentDataType.STRING, placeableNameMap.get(entityId), String.valueOf(entityId));
            NamespacedKeys.PLACEABLE_ITEM_LOCATION.set(chunk, new LocationTagType(), placeableItemLocationMap.get(entityId), String.valueOf(entityId));
            NamespacedKeys.PLACEABLE_ITEM_ROTATION.set(chunk, new QuaternionTagType(), placeableItemRotationMap.get(entityId), String.valueOf(entityId));
            NamespacedKeys.PLACEABLE_ITEM_STATE.set(chunk, PersistentDataType.INTEGER, placeableItemStateMap.get(entityId), String.valueOf(entityId));

            BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(placeableNameMap.get(entityId));
            placeableItem.hide(placeableItemLocationMap.get(entityId));
        }
        NamespacedKeys.PLACEABLE_ITEM_LIST.set(chunk, PersistentDataType.INTEGER_ARRAY, placeableItemsEntityIds);
    }

    public void writePlaceableItem(int entityId, String placeableName, Location location, Quaternionf rotation, int state) {
        placeableItemsEntityIds = BasicUtil.addElement(placeableItemsEntityIds, entityId);
        placeableNameMap.put(entityId, placeableName);
        placeableItemLocationMap.put(entityId, location);
        placeableItemRotationMap.put(entityId, rotation);
        placeableItemStateMap.put(entityId, state);
    }

    public void removePlaceableItem(int entityId) {
        placeableItemsEntityIds = BasicUtil.removeElement(placeableItemsEntityIds, entityId);
        placeableNameMap.remove(entityId);
        placeableItemLocationMap.remove(entityId);
        placeableItemRotationMap.remove(entityId);
        placeableItemStateMap.remove(entityId);
    }

    public void updateState(int entityId, int state) {
        placeableItemStateMap.put(entityId, state);
    }

    public void storeItemStack(int uniqueId, ItemStack itemStack) {
        chunk.getPersistentDataContainer().set(NamespacedKeys.ITEM_STACK.getNamespacedKey(String.valueOf(uniqueId)), ITEM_STACK_TAG_TYPE, itemStack);
    }

    public ItemStack getItemStack(int uniqueId) {
        if (chunk.getPersistentDataContainer().has(NamespacedKeys.ITEM_STACK.getNamespacedKey(String.valueOf(uniqueId)))) {
            return chunk.getPersistentDataContainer().get(NamespacedKeys.ITEM_STACK.getNamespacedKey(String.valueOf(uniqueId)), ITEM_STACK_TAG_TYPE);
        }
        return null;
    }

}
