package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
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
public class GrillTile extends BasePlaceableItemTile implements Triggerable {

    public GrillTile() {
        super();
        placeableName = "grill";
    }
    public GrillTile(int entityId) {
        super(entityId);
        placeableName = "grill";
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        super.display(location, rotation, state);
        sendBlockPacket(location);
    }

    private void sendBlockPacket(Location location) {
        if (!location.getBlock().getType().isAir()) {
            return;
        }
        if (!players.isEmpty()) {
            PacketUtil.changeBlock(players.stream().toList(), location, Material.BARRIER);
        }
    }

    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.GRILL.getCustomModelData(), null, null);
        List<Player> players = BasicUtil.getNearbyPlayers(location, 16);
        players.removeAll(this.players);
        if (!players.isEmpty()) {
            PacketUtil.spawnItemDisplay(players, location, itemStack, entityId, null, rotation);
        }
        this.players.addAll(players);
    }

    @Override
    public void hide(Location location) {
        super.hide(location);
        removeBlockPacket(location);
    }
    private void removeBlockPacket(Location location) {
        if (!location.getBlock().getType().isAir()) {
            return;
        }
        if (!players.isEmpty()) {
            PacketUtil.changeBlock(players.stream().toList(), location, Material.AIR);
        }
    }

    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players.stream().toList(), entityId);
        }
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        if (Objects.isNull(itemStack) || itemStack.getType().isAir()) {
            return false;
        }
        if (!itemStack.getType().isOccluding() && itemStack.getType().isBlock()) {
            location.getBlock().setType(itemStack.getType());
            return true;
        }
        return false;
    }
}
