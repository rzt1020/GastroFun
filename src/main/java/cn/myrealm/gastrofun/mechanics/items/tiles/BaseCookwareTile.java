package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.CompleteDisplayScheduler;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.TextDisplayScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public abstract class BaseCookwareTile extends BasePlaceableItemTile implements Triggerable, SchedulerAble {
    protected final List<BaseIngredient> ingredients = new ArrayList<>();
    protected CompleteDisplayScheduler completeDisplayScheduler;
    protected TextDisplayScheduler withoutContainerReminder;
    protected Food food;
    protected int recipeId;
    public BaseCookwareTile() {
        super();
    }

    public BaseCookwareTile(int entityId) {
        super(entityId);
    }

    @Override
    public boolean trigger(Player player, ItemStack itemStack, Location location) {
        if (Objects.nonNull(itemStack)) {
            itemStack = itemStack.clone();
        }
        if (Objects.isNull(food)) {
            return add(itemStack, location);
        }
        return take(player, itemStack, location);
    }
    
    
    protected abstract boolean add(ItemStack itemStack, Location location);

    protected boolean take(Player player, ItemStack itemStack, Location location) {
        ItemStack container = food.getContainer();
        if (Objects.isNull(container)) {
            completeDisplayScheduler.end();
            giveFood(player);
        } else {
            if (container.isSimilar(itemStack)) {
                completeDisplayScheduler.end();
                giveFood(player);
                return true;
            } else {
                completeDisplayScheduler.speedUp();
                if (Objects.isNull(withoutContainerReminder) || withoutContainerReminder.isEnd()) {
                    withoutContainerReminder = new TextDisplayScheduler(GastroFun.plugin, 1L, 60L, "请使用合适的容器来取得食物", entityId+6, location.clone().add(0, 0.5, 0), players.stream().toList());
                    withoutContainerReminder.play(0);
                } else {
                    withoutContainerReminder.updateEndTicks();
                }
            }
        }
        return false;
    }

    protected void giveFood(Player player) {
        ItemStack itemStack = (ItemStack) getResult();
        ingredients.clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Objects.isNull(itemStack)) {
                    return;
                }
                if (player.getInventory().getItemInMainHand().getType().isAir()) {
                    player.getInventory().setItemInMainHand(itemStack);
                } else {
                    player.getInventory().addItem(itemStack);
                }
                food = null;
            }
        }.runTaskLater(GastroFun.plugin, 1);

    }
}
