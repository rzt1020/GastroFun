package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.BaseMixingIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientFirst;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientSecond;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientThird;
import cn.myrealm.gastrofun.mechanics.misc.ProgressBar;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.*;
import cn.myrealm.gastrofun.mechanics.scheduler.processes.FailureReturnScheduler;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class MixingBowlTile extends BaseCookwareTile {
    private Quaternionf rotation;
    private final ProgressBar progressBar;

    public MixingBowlTile() {
        super();
        placeableName = "mixing_bowl";
        progressBar = new ProgressBar(entityId + 5);
    }
    public MixingBowlTile(int entityId) {
        super(entityId);
        placeableName = "mixing_bowl";
        progressBar = new ProgressBar(entityId + 5);
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

    @Override
    protected boolean add(ItemStack itemStack, Location location) {
        BaseMixingIngredient ingredient;
        switch (ingredients.size()) {
            case 0 -> {
                ingredient = new MixingIngredientFirst(entityId);
                ingredients.add(ingredient);
                new BlowMixingScheduler(GastroFun.plugin, 1, 330, rotation, entityId, location, players.stream().toList(), ingredients)
                        .with(new ProgressBarScheduler(GastroFun.plugin, 1L, 330L, progressBar, location, players.stream().toList()), 0L)
                        .with(new SpoonMixingScheduler(GastroFun.plugin, 1, 330, entityId + 1, players.stream().toList()), 0L)
                        .play(0L)
                        .complete(this)
                        .then(new CookingCompleteScheduler(GastroFun.plugin, 1L, 20L, entityId, entityId + 6, location, players.stream().toList(), ingredients))
                        .with(completeDisplayScheduler = new CompleteDisplayScheduler(GastroFun.plugin, 1L, -1L, entityId + 6, location.clone().add(0, 1, 0), players.stream().toList(), this), 10L)
                        .with(new TextDisplayScheduler(GastroFun.plugin, 1L, -1L, "x1", entityId + 7, location, players.stream().toList()), 10L)
                        .with(new CircularOffsetScheduler(GastroFun.plugin, 1L, -1L, 0.5, entityId + 7, location, players.stream().toList()), 10L)
                        .with(new FailureReturnScheduler(GastroFun.plugin, this, location.clone().add(0, 1, 0), ingredients), 10L);
            }
            case 1 -> {
                ingredient = new MixingIngredientSecond(entityId);
                ingredients.add(ingredient);
            }
            case 2 -> {
                ingredient = new MixingIngredientThird(entityId);
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
        this.rotation = rotation;
    }


    @Override
    public void sendEntityPacket(Location location, Quaternionf rotation, int state) {
        ItemStack itemStack = ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.MIXING_BOWL.getCustomModelData(), null, null);
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
    }


    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players.stream().toList(), entityId);
        }
    }

    @Override
    public void schedulerCompleted() {
        List<ItemStack> items = ingredients.stream().map(BaseIngredient::getItemStack).toList();
        Pair<Food, Integer> pair = FoodManager.getInstance().matchMixing(items);
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
        return food.getMixing(recipeId);
    }
}
