package cn.myrealm.gastrofun.mechanics.items.placeable.tiles;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.MixingBowlIngredient;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.misc.MixingStickEffect;
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
public class MixingBowlTile extends BasePlaceableItemTile implements Triggerable {
    private Location location;
    private Quaternionf rotation;
    private BaseIngredient ingredient;

    public MixingBowlTile() {
        super();
        placeableName = "mixing_bowl";
    }
    public MixingBowlTile(int entityId) {
        super(entityId);
        placeableName = "mixing_bowl";
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        if (Objects.nonNull(itemStack)) {
            itemStack = itemStack.clone();
        }
        return add(itemStack, location);
    }

    private boolean add(ItemStack itemStack, Location location) {
        if (Objects.nonNull(ingredient)) {
            return false;
        }
        ingredient = new MixingBowlIngredient(entityId);
        ingredient.setItemStack(itemStack);
        ingredient.display(players.stream().toList(), location);
        MixingStickEffect effect = new MixingStickEffect(entityId+2);
        effect.display(players.stream().toList(), location);
        effect.animate();
        return true;
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        super.display(location, rotation, state);
        sendBlockPacket(location);
        this.location = location;
        this.rotation = rotation;
    }

    private void sendBlockPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.changeBlock(players.stream().toList(), location, Material.BARRIER);
        }
    }

    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.MIXING_BOWL.getCustomModelData(), null, null);
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
}
