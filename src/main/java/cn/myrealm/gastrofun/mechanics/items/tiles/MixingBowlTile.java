package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.BaseMixingIngredient;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientFirst;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientSecond;
import cn.myrealm.gastrofun.mechanics.ingredients.mixing.MixingIngredientThird;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.misc.ProgressBar;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.BlowMixingScheduler;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.ProgressBarScheduler;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.SkilletCompleteScheduler;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.SpoonMixingScheduler;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
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
public class MixingBowlTile extends BasePlaceableItemTile implements Triggerable {
    private Location location;
    private Quaternionf rotation;
    private final ProgressBar progressBar;
    private final List<BaseIngredient> ingredients = new ArrayList<>();

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
        return add(itemStack, location);
    }

    private boolean add(ItemStack itemStack, Location location) {
        BaseMixingIngredient ingredient;
        switch (ingredients.size()) {
            case 0 -> {
                ingredient = new MixingIngredientFirst(entityId);
                ingredients.add(ingredient);
                new BlowMixingScheduler(GastroFun.plugin, 1, 330, rotation, entityId, location, players.stream().toList(), ingredients)
                        .with(new ProgressBarScheduler(GastroFun.plugin, 1L, 330L, progressBar, location, players.stream().toList()), 0L)
                        .with(new SpoonMixingScheduler(GastroFun.plugin, 1, 330, entityId + 1, players.stream().toList()), 0L)
                        .play(0)
                        .then(new SkilletCompleteScheduler(GastroFun.plugin, 1L, 20L, entityId, location, players.stream().toList(), ingredients));
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
        this.location = location;
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
}
