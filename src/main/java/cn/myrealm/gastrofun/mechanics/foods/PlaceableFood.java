package cn.myrealm.gastrofun.mechanics.foods;


import cn.myrealm.gastrofun.enums.mechanics.Fonts;
import cn.myrealm.gastrofun.enums.systems.Messages;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.managers.system.TextureManager;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rzt1020
 */
public class PlaceableFood extends Food {
    public PlaceableFood(FoodManager foodManager, YamlConfiguration config, String foodName) {
        super(foodManager, config, foodName);
    }

    @Override
    protected ItemStack createFoodItemStack(ConfigurationSection section) {
        String displayName = ConfigKeys.DISPLAY_NAME.asString(config);
        if (section.contains(ConfigKeys.DISPLAY_NAME.getKey())) {
            displayName = ConfigKeys.DISPLAY_NAME.asString(section);
        }
        String description = ConfigKeys.DESCRIPTION.asString(config);
        if (section.contains(ConfigKeys.DESCRIPTION.getKey())) {
            description = ConfigKeys.DESCRIPTION.asString(section);
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Messages.GAME_DESCRIPTION.getMessage());
        lore.addAll(BasicUtil.parsePrefix(BasicUtil.stringArray(description, 20), "&7"));

        int foodPoint = ConfigKeys.FOOD_POINT.asInt(section);
        if (foodPoint > 0) {
            lore.add("");
            lore.add(Messages.GAME_FOOD_POINT.getMessage());
            StringBuilder bar = new StringBuilder();
            bar.append(Fonts.HUNGER.toString().repeat(foodPoint / 2));
            if (foodPoint % 2 == 1) {
                bar.append(Fonts.HUNGER_HALF);
            }
            bar.append("x").append(ConfigKeys.MAX_SHARE.asInt(config));
            lore.addAll(BasicUtil.stringArray(bar.toString(), 10));
        }

        return ItemUtil.generateItemStack(Material.YELLOW_DYE, TextureManager.getInstance().getPlaceableFoodCustomModelData(foodName), displayName, lore);
    }

    @Override
    protected void packageData(ItemStack itemStack, ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        NamespacedKeys.PLACEABLE_NAME.set(itemMeta, PersistentDataType.STRING, "food_model:" + foodName);
        NamespacedKeys.PLACEABLE_FOOD_NAME.set(itemMeta, PersistentDataType.STRING, foodName);
        NamespacedKeys.FOOD_POINT.set(itemMeta, PersistentDataType.INTEGER, ConfigKeys.FOOD_POINT.asInt(section));
        NamespacedKeys.SATURATION.set(itemMeta, PersistentDataType.DOUBLE, ConfigKeys.SATURATION.asDouble(section));
        itemStack.setItemMeta(itemMeta);
    }

    public int getMaxShare() {
        return ConfigKeys.MAX_SHARE.asInt(config);
    }
}
