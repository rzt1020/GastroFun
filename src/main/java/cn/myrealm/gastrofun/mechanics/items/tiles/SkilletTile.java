package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.skillet.BaseSkilletIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.skillet.SkilletIngredientFirst;
import cn.myrealm.gastrofun.mechanics.ingredients.skillet.SkilletIngredientSecond;
import cn.myrealm.gastrofun.mechanics.ingredients.skillet.SkilletIngredientThird;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class SkilletTile extends BaseCookwareTile {
    private final ProgressBar progressBar;


    public SkilletTile() {
        super("skillet");
        progressBar = new ProgressBar(entityId + 4);
    }
    public SkilletTile(int entityId) {
        super(entityId, "skillet");
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
        List<Player> players1 = new ArrayList<>(players);
        players.removeAll(this.players);
        if (!players.isEmpty()) {
            PacketUtil.spawnItemDisplay(players, location, itemStack, entityId, null, rotation);
        }
        this.players.addAll(players);
        this.players.clear();
        this.players.addAll(players1);;
    }

    @Override
    public void hide(Location location) {
        super.hide(location);
        for (BaseIngredient ingredient : ingredients) {
            ingredient.hide(players);
        }
    }


    @Override
    public void removeEntityPacket(Location location) {
        if (!players.isEmpty()) {
            PacketUtil.removeEntity(players, entityId);
        }
    }


    @Override
    protected boolean add(ItemStack itemStack, Location location) {
        BaseSkilletIngredient ingredient;
        switch (ingredients.size()) {
            case 0 -> {
                ingredient = new SkilletIngredientFirst(entityId);
                ingredients.add(ingredient);
                CircularOffsetScheduler circularOffsetScheduler;
                new SkilletCookingScheduler(GastroFun.plugin, 1L, 330L, entityId, location, players, ingredients)
                        .with(new ProgressBarScheduler(GastroFun.plugin, 1L, 330L, progressBar, location, players), 0L)
                        .with(new LightUpEffectScheduler(GastroFun.plugin, 1L, 330L, 30, location), 0L)
                        .play(0L)
                        .complete(this)
                        .then(new CookingCompleteScheduler(GastroFun.plugin, 1L, 20L, entityId, entityId + 5, location, players, ingredients))
                        .with(completeDisplayScheduler = new CompleteDisplayScheduler(GastroFun.plugin, 1L, -1L, entityId + 5, location.clone().add(0, 1, 0), players, this), 10L)
                        .with(circularOffsetScheduler =new CircularOffsetScheduler(GastroFun.plugin, 1L, -1L, 0.5, entityId + 7, location, players), 10L)
                        .with(amountDisplayScheduler = new AmountDisplayScheduler(GastroFun.plugin, 1L, -1L,this, circularOffsetScheduler , entityId + 7, location, players), 10L)
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
        ingredient.display(players, location);
        return true;
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
        if (Objects.nonNull(foodStack)) {
            return foodStack;
        }
        if (Objects.isNull(food)) {
            return null;
        }
        foodStack = food.getFood(recipeId);
        return foodStack;
    }

}
