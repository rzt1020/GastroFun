package cn.myrealm.gastrofun.mechanics.scheduler;

import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public abstract class BaseScheduler extends BukkitRunnable {

    protected final JavaPlugin plugin;
    protected final long period;
    protected long endTicks;
    protected long count = 0;
    protected BaseScheduler nextScheduler;
    protected SchedulerAble schedulerAble;
    protected final List<BaseScheduler> parallelSchedulers = new ArrayList<>();
    protected final List<Long> parallelSchedulerTicks = new ArrayList<>();

    public BaseScheduler(JavaPlugin plugin, long period, long endTicks) {
        this.plugin = plugin;
        this.period = period;
        this.endTicks = endTicks;
    }

    public BaseScheduler then(BaseScheduler nextScheduler) {
        this.nextScheduler = nextScheduler;
        return this.nextScheduler;
    }

    public BaseScheduler with(BaseScheduler parallelScheduler, long delayTicks) {
        parallelSchedulers.add(parallelScheduler);
        parallelSchedulerTicks.add(delayTicks);
        return this;
    }

    public BaseScheduler play(long delayTicks) {
        this.runTaskTimer(plugin, delayTicks, period);
        for (int i=0; i<parallelSchedulers.size(); i++) {
            parallelSchedulers.get(i).play(parallelSchedulerTicks.get(i));
        }
        return this;
    }

    public BaseScheduler complete(SchedulerAble schedulerAble) {
        this.schedulerAble = schedulerAble;
        return this;
    }

    protected void end() {
        this.cancel();
        if (Objects.nonNull(schedulerAble)) {
            schedulerAble.schedulerCompleted();
        }
        if (Objects.nonNull(nextScheduler)) {
            nextScheduler.play(0);
        }
    }

    @Override
    public void run() {
        count += period;
        if (count >= endTicks) {
            end();
        }
    }

}
