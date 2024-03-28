package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author rzt1020
 */
public class TryRemoveScheduler extends BaseScheduler {
    private boolean yawControl;
    private float yaw;
    private final int entityId;
    private final List<Player> players;
    public TryRemoveScheduler(JavaPlugin plugin, long period, long endTicks, int entityId, List<Player> players) {
        super(plugin, period, endTicks);
        this.entityId = entityId;
        this.players = players;
    }
    @Override
    public void end() {
        PacketUtil.rotateEntity(players,  entityId, 0, 0);
        super.end();
    }

    @Override
    public void run() {
        yaw += yawControl ? 3 : -3;
        yaw %= 360;
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw == 3 || yaw == 357)  {
            yawControl = !yawControl;
        }

        PacketUtil.rotateEntity(players,  entityId, yaw, 0);
    }
}
