package cn.myrealm.gastrofun.mechanics.items.placeable.items;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.mechanics.items.DefaultItem;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.items.placeable.tiles.BasePlaceableItemTile;
import cn.myrealm.gastrofun.mechanics.items.placeable.tiles.GrillTile;
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
public class Grill extends BasePlaceableItem implements DefaultItem, Triggerable {
    public Grill() {
        super();
        placeableName = "grill";
    }

    @Override
    public ItemStack generate() {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.GRILL.getCustomModelData(), "&6Grill", null);

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        NamespacedKeys.PLACEABLE_NAME.set(itemMeta, PersistentDataType.STRING, DefaultItems.GRILL.getName());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public BasePlaceableItemTile createTile() {
        return new GrillTile();
    }

    @Override
    public BasePlaceableItemTile createTile(int entityId) {
        return new GrillTile(entityId);
    }

    @Override
    public Location getLocation(Block block, BlockFace blockFace) {
        if (block.getType().isOccluding()) {
            return block.getRelative(blockFace).getLocation();
        }
        return block.getLocation();
    }

    @Override
    public boolean mustAir() {
        return false;
    }

    @Override
    public boolean removable(ItemStack itemStack) {
        if (Objects.isNull(itemStack)) {
            return false;
        }
        return PICKAXES.contains(itemStack.getType());
    }

    @Override
    public boolean canPlacedAt(Location location) {
        return !location.getBlock().getType().isOccluding() || location.getBlock().getType().equals(Material.WATER) || location.getBlock().getType().equals(Material.LAVA);
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
}
