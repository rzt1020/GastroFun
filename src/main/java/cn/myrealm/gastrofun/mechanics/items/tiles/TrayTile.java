package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.tray.TrayIngredientFirst;
import cn.myrealm.gastrofun.mechanics.ingredients.tray.TrayIngredientSecond;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.misc.PlaceableChunk;
import cn.myrealm.gastrofun.mechanics.misc.ProgressBar;
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
public class TrayTile extends BasePlaceableItemTile implements Triggerable {
    private BaseIngredient ingredient;
    private ItemStack itemStack;
    private Quaternionf rotation;
    public TrayTile() {
        super("tray");
    }
    public TrayTile(int entityId) {
        super(entityId, "tray");
    }

    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        super.place(location, rotation, state, itemStack);
        WorldUtil.changeBlock(location, Material.TRIPWIRE);
    }

    @Override
    public void remove(Location location) {
        super.remove(location);
        WorldUtil.changeBlock(location, Material.AIR);
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        this.rotation = rotation.rotationY((float) (Math.PI / 2));
        super.display(location, rotation, state);

    }

    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.TRAY.getCustomModelData(), null, null);
        List<Player> players = BasicUtil.getNearbyPlayers(location, 16);
        players.removeAll(this.players);
        if (!players.isEmpty()) {
            PacketUtil.spawnItemDisplay(players, location, itemStack, entityId, null, rotation);
        }
        this.players.addAll(players);
    }

    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players, entityId);
        }
    }

    @Override
    public boolean isFunctioning() {
        return false;
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        if (Objects.nonNull(ingredient)) {
            if (player.isSneaking()) {
                if (ingredient instanceof TrayIngredientFirst) {
                    poseSecond(location);
                } else {
                    poseFirst(location);
                }
            }

            return false;
        }

        if (Objects.isNull(itemStack) || itemStack.getType() == Material.AIR) {
            return false;
        }
        this.itemStack = itemStack.clone();
        this.itemStack.setAmount(1);

        poseFirst(location);

        return true;
    }

    private void poseFirst(Location location) {
        ingredient = new TrayIngredientFirst(entityId, rotation);
        ingredient.setItemStack(itemStack);
        ingredient.display(players, location);
    }
    private void poseSecond(Location location) {
        ingredient = new TrayIngredientSecond(entityId, rotation);
        ingredient.setItemStack(itemStack);
        ingredient.display(players, location);
    }
}
