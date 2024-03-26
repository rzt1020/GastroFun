package cn.myrealm.gastrofun.mechanics.scheduler.processes;


import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class FailureReturnScheduler extends BaseScheduler {
    private final SchedulerAble schedulerAble;
    private final Location location;
    private final List<BaseIngredient> ingredients;
    public FailureReturnScheduler(JavaPlugin plugin, SchedulerAble schedulerAble, Location location, List<BaseIngredient> ingredients) {
        super(plugin, 1L, 1L);
        this.schedulerAble = schedulerAble;
        this.location = location;
        this.ingredients = ingredients;
    }

    @Override
    public void run() {
        if (Objects.nonNull(schedulerAble.getResult())) {
            end();
            return;
        }
        for (BaseIngredient ingredient : ingredients) {
            ItemStack itemStack = ingredient.getItemStack();
            itemStack.setAmount(1);
            Objects.requireNonNull(location.getWorld()).spawn(location, Item.class, item -> {
                item.setItemStack(itemStack);
                item.setVelocity(new Vector(0, 0.1, 0));
            });
        }
        ingredients.clear();
        super.run();
    }

}
