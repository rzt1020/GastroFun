package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class AmountDisplayScheduler extends TextDisplayScheduler {
    private final SchedulerAble schedulerAble;
    private final CircularOffsetScheduler circularOffsetScheduler;
    public AmountDisplayScheduler(JavaPlugin plugin, long period, long endTicks, SchedulerAble schedulerAble, CircularOffsetScheduler circularOffsetScheduler, int entityId, Location location, List<Player> players) {
        super(plugin, period, endTicks, "", entityId, location, players);
        this.schedulerAble = schedulerAble;
        this.circularOffsetScheduler = circularOffsetScheduler;
    }

    private String getAmount() {
        ItemStack itemStack = (ItemStack) schedulerAble.getResult();
        if (Objects.isNull(itemStack) || itemStack.getAmount() <= 1) {
            circularOffsetScheduler.end();
            end();
            return "";
        }
        return "x" + itemStack.getAmount();
    }
    public void updateAmount() {
        updateText(getAmount());
    }

    @Override
    public void run() {
        if (count == 0) {
            updateAmount();
        }
        super.run();
    }
}
