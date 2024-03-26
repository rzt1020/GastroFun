package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.misc.ProgressBar;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author rzt1020
 */
public class ProgressBarScheduler extends BaseScheduler {
    private final ProgressBar progressBar;
    private final Location location;
    private final List<Player> players;
    public ProgressBarScheduler(JavaPlugin plugin, long period, long endTicks, ProgressBar progressBar, Location location, List<Player> players) {
        super(plugin, period, endTicks);
        this.progressBar = progressBar;
        this.location = location.clone().add(0,0.5,0);
        this.players = players;
    }

    @Override
    public BaseScheduler play(long delayTicks) {
        progressBar.display(players, location);
        return super.play(delayTicks);
    }

    @Override
    public void run() {
        if (count % 30 == 0 && count != 0) {
            progressBar.addProgress();
        }
        progressBar.update(players,  location);
        super.run();
    }

    @Override
    public void end() {
        super.end();
        progressBar.reset();
    }
}
