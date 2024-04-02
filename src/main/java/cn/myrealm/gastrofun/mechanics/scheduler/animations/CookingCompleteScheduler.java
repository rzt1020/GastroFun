package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author rzt1020
 */
public class CookingCompleteScheduler extends BaseScheduler {
    private boolean up = true;
    private final int entityId;
    private final int displayEntityId;
    private final Location location;
    private final List<Player> players;
    private final List<BaseIngredient> ingredients;

    public CookingCompleteScheduler(JavaPlugin plugin, long period, long endTicks, int entityId, int displayEntityId, Location location, List<Player> players, List<BaseIngredient> ingredients) {
        super(plugin, period, endTicks);
        this.entityId = entityId;
        this.displayEntityId = displayEntityId;
        this.location = location.clone();
        this.players = players;
        this.ingredients = ingredients;
    }

    @Override
    public BaseScheduler play(long delayTicks) {
        PacketUtil.teleportEntity(players, entityId, location);
        return super.play(delayTicks);
    }

    @Override
    public void run() {
        Vector displacement = new Vector(0, up? 0.1 : -0.1, 0);
        Vector displacement2 = new Vector(0, up? 0.2 : -0.1, 0);
        PacketUtil.moveEntityWithPacket(players, entityId, displacement);
        PacketUtil.moveEntityWithPacket(players, displayEntityId, displacement);
        for (BaseIngredient ingredient : ingredients) {
            ingredient.move(players, displacement2, location);
        }
        if (count >= 9) {
            up = false;
            for (BaseIngredient ingredient : ingredients) {
                ingredient.hide(players);
            }
        }
        super.run();
    }
    @Override
    public void end() {
        super.end();
        PacketUtil.teleportEntity(players, entityId, location);
    }
}
