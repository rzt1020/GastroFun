package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

/**
 * @author rzt1020
 */
public class BlowMixingScheduler extends BaseScheduler {
    private final int entityId;
    private final Location location;
    private final List<Player> players;
    private double theta = 0;
    private final Quaternionf rotation;
    private final List<BaseIngredient> ingredients;
    public BlowMixingScheduler(JavaPlugin plugin, long period, long endTicks, Quaternionf rotation, int entityId, Location location, List<Player> players, List<BaseIngredient> ingredients) {
        super(plugin, period, endTicks);
        this.rotation = rotation;
        this.entityId = entityId;
        this.location = location.clone();
        this.players = players;
        this.ingredients = ingredients;
    }

    @Override
    public void end() {
        super.end();
        PacketUtil.removeEntity(players, entityId + 1);
        PacketUtil.tiltEntity(players, entityId, 0, 0);
    }

    @Override
    public BaseScheduler play(long delayTicks) {
        PacketUtil.spawnItemDisplay(players, location.clone().add(-0.01,0, -0.15), DefaultItems.MIXING_SPOON.generateItemStack(), entityId + 1,new Vector3f(0.75f), rotation);
        return super.play(delayTicks);
    }

    @Override
    public void run() {
        float pitch = 10 * (float)Math.sin(theta);
        float roll = 10 * (float)Math.cos(theta);

        PacketUtil.tiltEntity(players, entityId, roll, pitch);
        PacketUtil.tiltEntity(players, entityId+1, roll, pitch);

        for (BaseIngredient ingredient : ingredients) {
            ingredient.tilt(players, roll , pitch, location);
        }

        theta += Math.PI / 20;

        if (theta >= 2 * Math.PI) {
            theta = 0;
        }

        super.run();
    }
}
