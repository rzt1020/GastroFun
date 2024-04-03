package cn.myrealm.gastrofun.mechanics.ingredients.tray;

import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;


/**
 * @author rzt1020
 */
public class TrayIngredientFirst extends BaseIngredient {
    public TrayIngredientFirst(int entityId, Quaternionf rotation) {
        this.entityId = entityId+1;
        scale = new Vector3f(.4f, .4f, .4f);
        this.rotation = new Quaternionf().mul(rotation).rotateX((float)Math.PI / 2);
        offset = new Vector(0, -0.4, 0);
    }

    @Override
    public void display(List<Player> playerList, Location location) {
        location = location.clone().add(offset);
        Player player = BasicUtil.getNearestPlayer(location);
        if (Objects.isNull(player)) {
            return;
        }
        PacketUtil.spawnItemDisplay(playerList, location, itemStack, entityId, scale, rotation);
    }
}
