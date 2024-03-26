package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class CompleteDisplayScheduler extends BaseScheduler {
    private final int entityId;
    private final Location location;
    private final List<Player> players;
    private final SchedulerAble schedulerAble;
    private float yaw = 360;
    private float step = 3;
    public CompleteDisplayScheduler(JavaPlugin plugin, long period, long endTicks, int entityId, Location location, List<Player> players, SchedulerAble schedulerAble) {
        super(plugin, period, endTicks);
        this.entityId = entityId;
        this.location = location;
        this.players = players;
        this.schedulerAble = schedulerAble;
    }


    @Override
    public void run() {
        if (Objects.isNull(schedulerAble.getResult())) {
            end();
            return;
        }
        if (count == 0) {
            PacketUtil.spawnItemDisplay(players, location, (ItemStack) schedulerAble.getResult(), entityId, new Vector3f(0.5f,0.5f,0.5f),null);
        }
        if (yaw == 0) {
            yaw = 360;
        }
        PacketUtil.rotateEntity(players,  entityId, yaw, 0);
        yaw -= step;
        count ++;
    }

    @Override
    public void end() {
        super.end();
        PacketUtil.removeEntity(players, entityId);
    }

    boolean accelerate;
    public void speedUp() {
        if (step != 3) {
            return;
        }
        accelerate = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                step += accelerate ? 6 : -6;
                if (step >= 60) {
                    accelerate = false;
                }
                if (step == 3) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
