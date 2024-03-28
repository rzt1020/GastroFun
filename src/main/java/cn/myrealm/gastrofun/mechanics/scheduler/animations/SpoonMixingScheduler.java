package cn.myrealm.gastrofun.mechanics.scheduler.animations;

import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;


/**
 * @author rzt1020
 */
public class SpoonMixingScheduler extends BaseScheduler {
    private final int entityId;
    private final List<Player> players;
    private double angle = 0;
    public SpoonMixingScheduler(JavaPlugin plugin, long period, long endTicks, int entityId, List<Player> players) {
        super(plugin, period, endTicks);
        this.entityId = entityId;
        this.players = players;
    }

    @Override
    public void run() {
        double radius = 0.025;
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);

        Vector displacement = new Vector(x, 0, z);

        PacketUtil.moveEntityWithPacket(players, entityId, displacement);

        angle += Math.PI / 20;

        if (angle >= 2 * Math.PI) {
            angle = 0;
        }
        super.run();
    }
}
