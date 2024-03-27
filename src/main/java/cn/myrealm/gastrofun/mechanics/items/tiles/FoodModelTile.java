package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.listeners.FoodListener;
import cn.myrealm.gastrofun.managers.mechanics.ChunkManager;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.managers.system.TextureManager;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.foods.PlaceableFood;
import cn.myrealm.gastrofun.mechanics.persistent.ItemStackTagType;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class FoodModelTile extends BasePlaceableItemTile implements Triggerable {
    private final String placeableFoodName;
    private final PlaceableFood placeableFood;
    private Quaternionf rotation;
    private ItemStack placeableFoodItemStack;
    private final static ItemStackTagType ITEM_STACK_TAG_TYPE = new ItemStackTagType();
    public FoodModelTile(String placeableFoodName, PlaceableFood placeableFood) {
        super();
        this.placeableName = "food_model";
        this.placeableFoodName = placeableFoodName;
        this.placeableFood = placeableFood;
    }
    public FoodModelTile(int entityId, String placeableFoodName, PlaceableFood placeableFood) {
        super(entityId);
        this.placeableName = "food_model";
        this.placeableFoodName = placeableFoodName;
        this.placeableFood = placeableFood;
    }
    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        itemStack = itemStack.clone();
        ChunkManager.getInstance().getChunk(location.getChunk()).writePlaceableItem(entityId, placeableName+':'+placeableFoodName, location, rotation, state);
        location.getChunk().getPersistentDataContainer().set(NamespacedKeys.PLACEABLE_FOOD_ITEM_STACK.getNamespacedKey(String.valueOf(entityId)), ITEM_STACK_TAG_TYPE, itemStack);
        placeableFoodItemStack = itemStack;
        display(location, rotation, state);
        WorldUtil.changeBlock(location, Material.BARRIER);
    }

    @Override
    public void remove(Location location) {
        super.remove(location);
        WorldUtil.changeBlock(location, Material.AIR);
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        if (Objects.isNull(placeableFoodItemStack)) {
            placeableFoodItemStack = location.getChunk().getPersistentDataContainer().get(NamespacedKeys.PLACEABLE_FOOD_ITEM_STACK.getNamespacedKey(String.valueOf(entityId)), ITEM_STACK_TAG_TYPE);
        }
        super.display(location, rotation, state);
        this.rotation = rotation;
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        int maxShare = placeableFood.getMaxShare();
        state ++;

        if (player.getFoodLevel() >= 20) {
            return false;
        }
        FoodListener.handleEffects(player, placeableFoodItemStack);

        if (state >= maxShare) {
            PlaceableItemManager.getInstance().getPlaceableItem(placeableName+':'+placeableFoodName).remove(location);
        } else {
            ChunkManager.getInstance().getChunk(location.getChunk()).updateState(entityId, state);
            updateEntityPacket(location, rotation, state);
        }

        return false;
    }



    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        int cmd = TextureManager.getInstance().getFoodCustomModelData(placeableFoodName + '_' + state);
        ItemStack itemStack = ItemUtil.generateItemStack(Material.YELLOW_DYE, cmd, null, null);
        List<Player> players = BasicUtil.getNearbyPlayers(location, 16);
        players.removeAll(this.players);
        if (!players.isEmpty()) {
            PacketUtil.spawnItemDisplay(players, location, itemStack, entityId, null, rotation);
        }
        this.players.addAll(players);
    }

    public void updateEntityPacket(Location location, Quaternionf rotation, int state) {
        int cmd = TextureManager.getInstance().getFoodCustomModelData(placeableFoodName + '_' + state);
        ItemStack itemStack = ItemUtil.generateItemStack(Material.YELLOW_DYE, cmd, null, null);

        if (!players.isEmpty()) {
            PacketUtil.updateItemDisplay(players.stream().toList(), itemStack, entityId, null, rotation);
        }
    }

    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players.stream().toList(), entityId);
        }
    }
}
