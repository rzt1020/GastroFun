package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.mechanics.misc.MixingSpoon;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author rzt1020
 */
public class BlowMixingScheduler extends BaseScheduler {
    private final int entityId;
    private final Location location;
    private final List<Player> players;
    private double theta = 0;
    private final MixingSpoon spoon;
    public BlowMixingScheduler(JavaPlugin plugin, long period, long endTicks, MixingSpoon spoon, int entityId, Location location, List<Player> players) {
        super(plugin, period, endTicks);
        this.spoon = spoon;
        this.entityId = entityId;
        this.location = location;
        this.players = players;
    }

    @Override
    public void run() {
        float pitch = 10 * (float)Math.sin(theta);
        float roll = 10 * (float)Math.cos(theta);

        PacketUtil.tiltEntity(players, entityId, roll, pitch);
        PacketUtil.tiltEntity(players, entityId+1, roll, pitch);

        theta += Math.PI / 20;

        if (theta >= 2 * Math.PI) {
            theta = 0;
        }
    }
}
