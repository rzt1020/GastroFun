package cn.myrealm.gastrofun.mechanics.foods;


import cn.myrealm.gastrofun.enums.mechanics.Fonts;
import cn.myrealm.gastrofun.enums.systems.Messages;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.managers.system.TextureManager;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.ItemUtil;
import com.google.common.base.Strings;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class Food {
    protected final YamlConfiguration config;
    protected final FoodManager foodManager;
    protected final String foodName;
    public Food(FoodManager foodManager, YamlConfiguration config, String foodName) {
        this.foodManager = foodManager;
        this.config = config;
        this.foodName = foodName;
        init();
    }

    private void init() {
        int i = 1;
        while (config.contains(ConfigKeys.RECIPES.getKey() + i)) {
            ConfigurationSection section = config.getConfigurationSection(ConfigKeys.RECIPES.getKey() + i);
            if (section == null) {
                break;
            }
            List<String> ingredients = ConfigKeys.INGREDIENTS.asStringList(section);
            try {
                if (ingredients.isEmpty()) {
                    throw new Exception("Ingredients is empty");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            foodManager.registerRecipe(ingredients, foodName + ":" + i);
            i ++;
        }
        i = 1;
        while (config.contains(ConfigKeys.MIXING.getKey() + i)) {
            ConfigurationSection section = config.getConfigurationSection(ConfigKeys.MIXING.getKey() + i);
            if (section == null) {
                break;
            }
            List<String> ingredients = ConfigKeys.INGREDIENTS.asStringList(section);
            try {
                if (ingredients.isEmpty()) {
                    throw new Exception("Ingredients is empty");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            foodManager.registerMixing(ingredients, foodName + ":" + i);
            i ++;
        }
    }

    public ItemStack getFood(int recipeId) {
        ConfigurationSection section = config.getConfigurationSection(ConfigKeys.RECIPES.getKey() + recipeId);
        if (Objects.isNull(section)) {
            return null;
        }
        ItemStack itemStack = createFoodItemStack(section);
        packageData(itemStack, section);
        return itemStack;
    }
    public ItemStack getMixing(int mixingId) {
        ConfigurationSection section = config.getConfigurationSection(ConfigKeys.MIXING.getKey() + mixingId);
        if (Objects.isNull(section)) {
            return null;
        }
        ItemStack itemStack = createFoodItemStack(section);
        packageData(itemStack, section);
        return itemStack;
    }

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
        if (!Strings.isNullOrEmpty(description)) {
            lore.add(Messages.GAME_DESCRIPTION.getMessage());
            lore.addAll(BasicUtil.parsePrefix(BasicUtil.stringArray(description, 20), "&7"));
        }

        int foodPoint = ConfigKeys.FOOD_POINT.asInt(section);
        if (foodPoint > 0) {
            lore.add("");
            lore.add(Messages.GAME_FOOD_POINT.getMessage());
            StringBuilder bar = new StringBuilder();
            bar.append(Fonts.HUNGER.toString().repeat(foodPoint / 2));
            if (foodPoint % 2 == 1) {
                bar.append(Fonts.HUNGER_HALF);
            }
            lore.addAll(BasicUtil.stringArray(bar.toString(), 10));
        }

        if (ConfigKeys.STACKABLE.asBoolean(config)) {
            return ItemUtil.generateItemStack(Material.MELON_SLICE, TextureManager.getInstance().getFoodCustomModelData(foodName), displayName, lore);
        }
        return ItemUtil.generateItemStack(Material.MUSHROOM_STEW, TextureManager.getInstance().getFoodCustomModelData(foodName), displayName, lore);
    }
    protected void packageData(ItemStack itemStack, ConfigurationSection section) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        NamespacedKeys.FOOD_NAME.set(itemMeta, PersistentDataType.STRING, foodName);
        NamespacedKeys.FOOD_POINT.set(itemMeta, PersistentDataType.INTEGER, ConfigKeys.FOOD_POINT.asInt(section));
        NamespacedKeys.SATURATION.set(itemMeta, PersistentDataType.DOUBLE, ConfigKeys.SATURATION.asDouble(section));
        itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getContainer() {
        String container = ConfigKeys.CONTAINER.asString(config);
        if (Objects.isNull(container) || "".equals(container)) {
            return null;
        }
        return ItemUtil.generateItemStack(Material.getMaterial(container.toUpperCase()), null, null, null);
    }

    enum ConfigKeys {
        // food keys
        RECIPES("recipes.", null),
        MIXING("mixing.", null),
        CONTAINER("container", null),
        STACKABLE("stackable", false),
        INGREDIENTS("ingredients", null),
        DISPLAY_NAME("display_name", ""),
        DESCRIPTION("description", ""),
        FOOD_POINT("food_point", 0),
        SATURATION("saturation", 0.0D),
        // placeable food keys
        MAX_SHARE("max_share", 1);
        private final String key;
        private final Object def;

        ConfigKeys(String key, @Nullable Object def) {
            this.key = key;
            this.def = def;
        }
        public String getKey() {
            return key;
        }
        public String asString(ConfigurationSection config) {
            if (Objects.isNull(config)) {
                return (String) def;
            }
            return config.getString(key, (String) def);
        }
        public List<String> asStringList(ConfigurationSection config) {
            return config.getStringList(key);
        }
        public int asInt(ConfigurationSection config) {
            return  Objects.isNull(def) ? config.getInt(key, 0) : config.getInt(key, (Integer) def);
        }
        public double asDouble(ConfigurationSection config) {
            return  Objects.isNull(def) ? config.getDouble(key, 0.0D) : config.getDouble(key, (Double) def);
        }

        public boolean asBoolean(ConfigurationSection config) {
            return  Objects.isNull(def) ? config.getBoolean(key, false) : config.getBoolean(key, (Boolean) def);
        }
    }
}
