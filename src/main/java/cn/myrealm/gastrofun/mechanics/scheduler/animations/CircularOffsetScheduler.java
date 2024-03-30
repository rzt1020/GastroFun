package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author rzt1020
 */
public class CircularOffsetScheduler extends BaseScheduler {
    private final double length;
    private final int entityId;
    private final Location location;
    private final List<Player> players;
    public CircularOffsetScheduler(JavaPlugin plugin, long period, long endTicks,  double length, int entityId, Location location, List<Player> players) {
        super(plugin, period, endTicks);
        this.length = length;
        this.entityId = entityId;
        this.location = location.clone();
        this.players = players;
    }

    public Location offsetLocationByPolarCoordinates(Location originalLocation, double angleInDegrees) {
        double angleInRadians = Math.toRadians(angleInDegrees);

        double offsetX = length * Math.cos(angleInRadians);
        double offsetZ = length * Math.sin(angleInRadians);


        return originalLocation.clone().add(offsetX, 0, offsetZ);
    }
    public double getAngleBetweenLocations(Location loc1, Location loc2) {
        double deltaX = loc2.getX() - loc1.getX();
        double deltaZ = loc2.getZ() - loc1.getZ();

        double angleInRadians = Math.atan2(deltaZ, deltaX);

        double angleInDegrees = Math.toDegrees(angleInRadians);

        if (angleInDegrees < 0) {
            angleInDegrees += 360.0;
        }

        return angleInDegrees;
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public void run() {
        for (Player player : players) {
            PacketUtil.teleportEntity(List.of(player), entityId, offsetLocationByPolarCoordinates(location, (getAngleBetweenLocations(location, player.getLocation()) - 90) % 360));
        }
        super.run();
    }
}
