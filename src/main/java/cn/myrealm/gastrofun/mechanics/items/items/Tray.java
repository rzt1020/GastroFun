package cn.myrealm.gastrofun.mechanics.items.items;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.items.DefaultItem;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.items.tiles.BasePlaceableItemTile;
import cn.myrealm.gastrofun.mechanics.items.tiles.TrayTile;
import cn.myrealm.gastrofun.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

/**
 * @author rzt1020
 */
public class Tray extends BasePlaceableItem implements DefaultItem, Triggerable {
    public Tray() {
        super("tray");
    }
    @Override
    public BasePlaceableItemTile createTile() {
        return new TrayTile();
    }

    @Override
    public BasePlaceableItemTile createTile(int entityId) {
        return new TrayTile(entityId);
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

    @Override
    public ItemStack generate() {
        return ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.TRAY.getCustomModelData(), null, null);
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        if (Objects.isNull(itemStack) && !player.getInventory().getItemInMainHand().getType().isAir()) {
            return false;
        }
        BasePlaceableItemTile tile = tiles.get(location);
        if (Objects.isNull(tile)) {
            return false;
        }
        if (tile instanceof Triggerable triggerable) {
            return triggerable.trigger(player, itemStack, location);
        }
        return false;
    }
}
