package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.mechanics.ingredients.*;
import cn.myrealm.gastrofun.mechanics.items.SchedulerAble;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import cn.myrealm.gastrofun.mechanics.misc.ProgressBar;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.*;
import cn.myrealm.gastrofun.mechanics.scheduler.processes.FailureReturnScheduler;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class SkilletTile extends BasePlaceableItemTile implements Triggerable, SchedulerAble {
    private final ProgressBar progressBar;

    private final List<BaseIngredient> ingredients = new ArrayList<>();

    private Food food;
    private int recipeId;
    private CompleteDisplayScheduler completeDisplayScheduler;
    private TextDisplayScheduler withoutContainerReminder;

    public SkilletTile() {
        super();
        placeableName = "skillet";
        progressBar = new ProgressBar(entityId + 4);
    }
    public SkilletTile(int entityId) {
        super(entityId);
        placeableName = "skillet";
        progressBar = new ProgressBar(entityId + 4);
    }

    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        super.place(location, rotation, state, itemStack);
        WorldUtil.changeBlock(location, Material.BARRIER);
    }

    @Override
    public void remove(Location location) {
        super.remove(location);
        WorldUtil.changeBlock(location, Material.AIR);
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        super.display(location, rotation, state);
    }


    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.SKILLET.getCustomModelData(), null, null);
        List<Player> players = BasicUtil.getNearbyPlayers(location, 16);
        players.removeAll(this.players);
        if (!players.isEmpty()) {
            PacketUtil.spawnItemDisplay(players, location, itemStack, entityId, null, rotation);
        }
        this.players.addAll(players);
    }

    @Override
    public void hide(Location location) {
        super.hide(location);
        for (BaseIngredient ingredient : ingredients) {
            ingredient.hide(players.stream().toList());
        }
    }


    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players.stream().toList(), entityId);
        }
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

    private boolean add(ItemStack itemStack, Location location) {
        BaseSkilletIngredient ingredient;
        switch (ingredients.size()) {
            case 0 -> {
                ingredient = new SkilletIngredientFirst(entityId);
                ingredients.add(ingredient);
                new SkilletCookingScheduler(GastroFun.plugin, 1L, 330L, entityId, location, players.stream().toList(), ingredients)
                        .with(new ProgressBarScheduler(GastroFun.plugin, 1L, 330L, progressBar, location, players.stream().toList()), 0L)
                        .with(new LightUpEffectScheduler(GastroFun.plugin, 1L, 330L, 30, location), 0L)
                        .play(0L)
                        .complete(this)
                        .then(new SkilletCompleteScheduler(GastroFun.plugin, 1L, 20L, entityId, location, players.stream().toList(), ingredients))
                        .with(completeDisplayScheduler = new CompleteDisplayScheduler(GastroFun.plugin, 1L, -1L, entityId + 5, location.clone().add(0, 1, 0), players.stream().toList(), this), 10L)
                        .with(new FailureReturnScheduler(GastroFun.plugin, this, location.clone().add(0, 1, 0), ingredients), 10L);
            }
            case 1 -> {
                ingredient = new SkilletIngredientSecond(entityId);
                ingredients.add(ingredient);
            }
            case 2 -> {
                ingredient = new SkilletIngredientThird(entityId);
                ingredients.add(ingredient);
            }
            default -> {
                return false;
            }
        }
        ingredient.setItemStack(itemStack);
        ingredient.display(players.stream().toList(), location);
        return true;
    }

    private boolean take(Player player, ItemStack itemStack, Location location) {
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

    private void giveFood(Player player) {
        ItemStack itemStack = food.getFood(recipeId);
        ingredients.clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Objects.isNull(food)) {
                    return;
                }
                if (player.getInventory().getItemInMainHand().getType().isAir()) {
                    player.getInventory().setItemInMainHand(itemStack);
                } else {
                    player.getInventory().addItem(food.getFood(recipeId));
                }
                food = null;
            }
        }.runTaskLater(GastroFun.plugin, 1);

    }

    @Override
    public void schedulerCompleted() {
        List<ItemStack> items = ingredients.stream().map(BaseIngredient::getItemStack).toList();
        Pair<Food, Integer> pair = FoodManager.getInstance().matchFood(items);
        if (Objects.isNull(pair)) {
            food = null;
            return;
        }
        food = pair.getFirst();
        recipeId = pair.getSecond();
    }

    @Override
    public Object getResult() {
        if (Objects.isNull(food)) {
            return null;
        }
        return food.getFood(recipeId);
    }
}
