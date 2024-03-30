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
public class TextDisplayScheduler extends BaseScheduler {

    private String text;
    private final int entityId;
    private final Location location;
    private final List<Player> players;
    private final long endTicksBackUp;
    public TextDisplayScheduler(JavaPlugin plugin, long period, long endTicks, String text, int entityId, Location location, List<Player> players) {
        super(plugin, period, endTicks);
        this.text = text;
        this.entityId = entityId;
        this.location = location.clone();
        this.players = players;
        this.endTicksBackUp = endTicks;
    }

    @Override
    public void end() {
        super.end();
        PacketUtil.removeEntity(players, entityId);
    }

    public void updateEndTicks() {
        endTicks = count + endTicksBackUp;
    }
    public  void updateText(String text) {
        this.text = text;
    }

    @Override
    public void run() {
        if (count == 0) {
            PacketUtil.spawnTextDisplay(players, location, text, entityId, null, null);
        }
        for (Player player : players) {
            PacketUtil.updateTextDisplay(List.of(player), text, entityId, null, BasicUtil.directionToQuaternionDirectlyY(location, player.getLocation()));
        }
        super.run();
    }

    public boolean isEnd() {
        return isCancelled();
    }
}
