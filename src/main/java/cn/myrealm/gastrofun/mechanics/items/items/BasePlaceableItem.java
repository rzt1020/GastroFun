package cn.myrealm.gastrofun.mechanics.items.items;


import cn.myrealm.gastrofun.mechanics.items.Placeable;
import cn.myrealm.gastrofun.mechanics.items.tiles.BasePlaceableItemTile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

import java.util.*;

/**
 * @author rzt1020
 */
public abstract class BasePlaceableItem implements Placeable {
    protected final Set<Material> PICKAXES = new HashSet<>(List.of(
            Material.WOODEN_PICKAXE,
            Material.STONE_PICKAXE,
            Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE,
            Material.DIAMOND_PICKAXE,
            Material.NETHERITE_PICKAXE
    ));
    protected Map<Location , BasePlaceableItemTile> tiles;
    protected String placeableName;

    public BasePlaceableItem() {
        placeableName = "";
        tiles = new HashMap<>();
    }

    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        BasePlaceableItemTile tile = createTile();
        tile.place(location, rotation, state, itemStack);
        tiles.put(location, tile);
    }

    @Override
    public void remove(Location location) {
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return;
        }
        tile.remove(location);
        tiles.remove(location);
    }

    public void hideAll() {
        for (Location location : tiles.keySet()) {
            tiles.get(location).hide(location);
        }
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        BasePlaceableItemTile tile;
        if (tiles.containsKey(location)) {
            tile = tiles.get(location);
        } else  {
            tile = createTile();
        }
        tile.display(location, rotation, state);
        tiles.put(location, tile);
    }

    public void display(Location location, Quaternionf rotation, int state, int entityId) {
        BasePlaceableItemTile tile;
        if (tiles.containsKey(location)) {
            tile = tiles.get(location);
        } else  {
            tile = createTile(entityId);
        }
        tile.display(location, rotation, state);
        tiles.put(location, tile);
    }

    @Override
    public void hide(Location location) {
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return;
        }
        tile.hide(location);
        tiles.remove(location);
    }

    public String getPlaceableName() {
        return placeableName;
    }

    /**
     * create the tile of the item
     * @return item tile
     */
    public abstract BasePlaceableItemTile createTile();

    /**
     * create the tile of the item
     * @param entityId entity id
     * @return item tile
     */

    public abstract BasePlaceableItemTile createTile(int entityId);

    /**
     * get the location item should be placed at
     * @param block clicked block
     * @param blockFace clicked block face
     * @return item location
     */
    public abstract Location getLocation(Block block, BlockFace blockFace);

    /**
     * is the location be placed must be air
     * @return is the location be placed must be air
     */
    public abstract boolean mustAir();
    public boolean isPlacedAt(Location location) {
        return tiles.containsKey(location);
    }

    /**
     * is the item can be removed by this item
     * @param itemStack tool
     * @return true if item can be removed
     */
    public abstract boolean removable(ItemStack itemStack);

    /**
     * can the item be placed at the location
     * @param location the location
     * @return true if the item can be placed at the location
     */
    public abstract boolean canPlacedAt(Location location);

    public void tryRemove(Location location) {
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return;
        }
        tile.tryRemove();
    }

    public void giveUpRemove(Location location) {
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return;
        }
        tile.giveUpRemove();
    }

    public BasePlaceableItemTile getTile(Location location) {
        return tiles.get(location);
    }

}
