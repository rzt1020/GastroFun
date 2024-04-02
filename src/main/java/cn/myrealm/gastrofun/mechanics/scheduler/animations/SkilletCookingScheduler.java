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
public class SkilletCookingScheduler extends BaseScheduler {
    private double theta = 0;
    private final List<Player> players;
    private final int entityId;
    private final List<BaseIngredient> ingredients;
    private final Location location;

    public SkilletCookingScheduler(JavaPlugin plugin, long period, long endTicks, int entityId, Location location, List<Player> players, List<BaseIngredient> ingredients) {
        super(plugin, period, endTicks);
        this.entityId = entityId;
        this.location = location.clone();
        this.players = players;
        this.ingredients = ingredients;
    }

    @Override
    public BaseScheduler play(long delayTicks) {
        PacketUtil.teleportEntity(players, entityId, location.add(-0.01, 0, 0.05));
        return super.play(delayTicks);
    }

    @Override
    public void run() {
        double r = 0.025 * Math.cos(2 * theta);

        double x = r * Math.cos(theta);
        double z = r * Math.sin(theta);

        Vector displacement = new Vector(x, 0, z);

        PacketUtil.moveEntityWithPacket(players, entityId, displacement);
        for (BaseIngredient ingredient : ingredients) {
            ingredient.move(players.stream().toList(), displacement, location);
        }

        theta += Math.PI / 20;

        if (theta >= 2 * Math.PI) {
            theta = 0;
        }
        super.run();
    }
}
