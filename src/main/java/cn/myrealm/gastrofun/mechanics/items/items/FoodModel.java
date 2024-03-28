package cn.myrealm.gastrofun.mechanics.items.items;

import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.foods.PlaceableFood;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.items.tiles.BasePlaceableItemTile;
import cn.myrealm.gastrofun.mechanics.items.tiles.FoodModelTile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author rzt1020
 */
public class FoodModel extends BasePlaceableItem implements Triggerable {
    private final String placeableFoodName;
    private final PlaceableFood placeableFood;
    public FoodModel(String placeableFoodName, PlaceableFood placeableFood) {
        super();
        placeableName = "food_model";
        this.placeableFoodName = placeableFoodName;
        this.placeableFood = placeableFood;
    }

    @Override
    public String getPlaceableName() {
        return placeableName + ':' + placeableFoodName;
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return false;
        }
        if (tile instanceof Triggerable triggerable) {
            return triggerable.trigger(player, itemStack, location);
        }
        return false;
    }

    @Override
    public BasePlaceableItemTile createTile() {
        return new FoodModelTile(placeableFoodName, placeableFood);
    }

    @Override
    public BasePlaceableItemTile createTile(int entityId) {
        return new FoodModelTile(entityId, placeableFoodName, placeableFood);
    }

    @Override
    public Location getLocation(Block block, BlockFace blockFace) {
        block = block.getRelative(blockFace);
        if (blockFace.equals(BlockFace.UP)) {
            if (block.getType().equals(Material.WATER) || block.getType().equals(Material.LAVA)) {
                block = block.getRelative(blockFace);
            }
        }
        return block.getLocation();
    }

    @Override
    public boolean mustAir() {
        return true;
    }

    @Override
    public boolean removable(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canPlacedAt(Location location) {
        Block block = location.getBlock().getRelative(BlockFace.DOWN);
        BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(block.getLocation());
        if (Objects.nonNull(placeableItem) && DefaultItems.GRILL.getName().equals(placeableItem.getPlaceableName())) {
            return true;
        }
        return block.getType().isOccluding();
    }
}
