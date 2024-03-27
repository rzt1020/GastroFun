package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.misc.MixingSpoon;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.BlowMixingScheduler;
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
        MixingSpoon mixingSpoon = new MixingSpoon(players.stream().toList(), entityId+1, location, rotation);
        mixingSpoon.display();
        mixingSpoon.animate();
        new BlowMixingScheduler(GastroFun.plugin, 1, 330, mixingSpoon, entityId, location, players.stream().toList()).play(0);
        return true;
    }

    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        super.place(location, rotation, state, itemStack);
        WorldUtil.changeBlock(location, Material.BARRIER);
    }

    @Override
    public void remove(Location location) {
        super.remove(location);
        WorldUtil.changeBlock(location, Material.AIR);
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        super.display(location, rotation, state);
        this.location = location;
        this.rotation = rotation;
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
    }


    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players.stream().toList(), entityId);
        }
    }
}
