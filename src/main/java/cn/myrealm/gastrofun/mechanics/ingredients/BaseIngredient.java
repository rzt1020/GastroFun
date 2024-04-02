package cn.myrealm.gastrofun.mechanics.ingredients;

import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rzt1020
 */
public abstract class BaseIngredient {
    protected Vector3f scale = new Vector3f(1,1,1);
    protected Quaternionf rotation = new Quaternionf();
    protected Vector offset = new Vector();
    protected ItemStack itemStack;
    protected int entityId;
    protected final List<Player> players = new ArrayList<>();

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void display(List<Player> playerList, Location location) {
        location = location.clone().add(offset);
        Player player = BasicUtil.getNearestPlayer(location);
        if (Objects.isNull(player)) {
            return;
        }
        rotation.mul(BasicUtil.directionToQuaternionDirectlyZ(location, player.getLocation()));
        PacketUtil.spawnItemDisplay(playerList, location, itemStack, entityId, scale, rotation);
    }

    public void hide(List<Player> playerList) {
        PacketUtil.removeEntity(playerList, entityId);
    }

    public void move(List<Player> players, Vector displacement, Location location) {
        update(players, location);
        PacketUtil.moveEntityWithPacket(players, entityId, displacement);
    }


    public void tilt(List<Player> players, float roll, float pitch, Location location) {
        update(players, location);
        PacketUtil.tiltEntity(players, entityId, roll, pitch);
    }

    private void update(List<Player> players, Location location) {
        List<Player> newPlayers = players.stream()
                .filter(player -> !this.players.contains(player))
                .collect(Collectors.toList());

        display(newPlayers, location);

        this.players.clear();
        this.players.addAll(players);
    }

}
