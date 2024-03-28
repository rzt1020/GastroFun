package cn.myrealm.gastrofun.listeners;


import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * @author rzt1020
 */
public class FoodListener extends BaseListener {
    public FoodListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFoodEat(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack.hasItemMeta() && NamespacedKeys.FOOD_NAME.has(Objects.requireNonNull(itemStack.getItemMeta()), PersistentDataType.STRING)) {
            Player player = event.getPlayer();

            event.setCancelled(true);
            consumeItem(player, itemStack);
            handleEffects(player, itemStack);

            String foodName = (String) NamespacedKeys.FOOD_NAME.get(Objects.requireNonNull(itemStack.getItemMeta()), PersistentDataType.STRING);
            ItemStack container = FoodManager.getInstance().getFood(foodName).getContainer();

            if (Objects.nonNull(container)) {
                player.getInventory().addItem(container);
            }
        }
    }




    public static void handleEffects(Player player, ItemStack itemStack) {
        int foodPoint = (Integer) NamespacedKeys.FOOD_POINT.get(Objects.requireNonNull(itemStack.getItemMeta()), PersistentDataType.INTEGER);
        int playerFoodLevel = Math.min(player.getFoodLevel() + foodPoint, 20);
        player.setFoodLevel(playerFoodLevel);

        double saturation = (Double) NamespacedKeys.SATURATION.get(Objects.requireNonNull(itemStack.getItemMeta()), PersistentDataType.DOUBLE);
        double playerSaturation = Math.min(player.getSaturation() + saturation, playerFoodLevel);
        player.setSaturation((float) playerSaturation);
    }
}
