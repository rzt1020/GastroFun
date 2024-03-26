package cn.myrealm.gastrofun.mechanics.ingredients;

import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

/**
 * @author rzt1020
 */
public abstract class BaseIngredient {
    protected Vector3f scale = new Vector3f(1,1,1);
    protected Quaternionf rotation = new Quaternionf();
    protected Vector offset = new Vector();
    protected ItemStack itemStack;
    protected int entityId;

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void display(List<Player> playerList, Location location) {
        location = location.clone().add(offset);
        rotation.mul(BasicUtil.directionToQuaternionDirectlyZ(location, BasicUtil.getNearestPlayer(location).getLocation()));
        PacketUtil.spawnItemDisplay(playerList, location, itemStack, entityId, scale, rotation);
    }

    public void hide(List<Player> playerList) {
        PacketUtil.removeEntity(playerList, entityId);
    }

    public void move(List<Player> players, Vector displacement) {
        PacketUtil.moveEntityWithPacket(players, entityId, displacement);
    }
}
