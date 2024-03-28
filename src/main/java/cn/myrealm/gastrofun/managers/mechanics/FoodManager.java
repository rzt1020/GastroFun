package cn.myrealm.gastrofun.managers.mechanics;


import cn.myrealm.gastrofun.managers.BaseManager;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import cn.myrealm.gastrofun.mechanics.foods.PlaceableFood;
import cn.myrealm.gastrofun.mechanics.items.items.FoodModel;
import cn.myrealm.gastrofun.utils.BasicUtil;
import com.comphenix.protocol.wrappers.Pair;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

/**
 * @author rzt1020
 */
public class FoodManager extends BaseManager {
    private static FoodManager instance;
    private Map<String, Food> foodMap;
    private Map<String, String> recipeMap;
    private Map<String, String> mixingMap;
    public FoodManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    public static FoodManager getInstance() {
        return instance;
    }

    private static final String FOODS_PATH = "plugins/GastroFun/foods";
    private static final String PLACEABLE_FOODS_PATH = "plugins/GastroFun/placeable_foods";
    @Override
    protected void onInit() {
        foodMap = new HashMap<>(5);
        recipeMap = new HashMap<>(5);
        mixingMap = new HashMap<>(5);

        loadFoods();
        loadPlaceableFoods();
    }

    private void loadFoods() {
        File folder = new File(FOODS_PATH);
        File[] files = folder.listFiles();
        if (Objects.isNull(files)) {
            return;
        }
        for (File file : files) {
            String foodName = file.getName().replace(".yml", "");
            foodMap.put(foodName, new Food(this, YamlConfiguration.loadConfiguration(file), foodName));
        }
    }

    private void loadPlaceableFoods() {
        File folder = new File(PLACEABLE_FOODS_PATH);
        File[] files = folder.listFiles();
        if (Objects.isNull(files)) {
            return;
        }
        for (File file : files) {
            String foodName = file.getName().replace(".yml", "");
            PlaceableFood placeableFood = new PlaceableFood(this, YamlConfiguration.loadConfiguration(file), foodName);
            foodMap.put(foodName, placeableFood);
            PlaceableItemManager.getInstance().registerPlaceableItem(new FoodModel(foodName, placeableFood));
        }
    }


    public void registerRecipe(List<String> ingredients, String s) {
        recipeMap.put(BasicUtil.listToStringKey(ingredients), s);
    }
    public void registerMixing(List<String> ingredients, String s) {
        mixingMap.put(BasicUtil.listToStringKey(ingredients), s);
    }

    public Food getFood(String foodName) {
        return foodMap.get(foodName);
    }
    public Collection<String> getNames() {
        return foodMap.keySet();
    }

    public Pair<Food, Integer> matchFood(List<ItemStack> items) {
        return match(recipeMap, items);
    }
    public Pair<Food, Integer> matchMixing(List<ItemStack> items) {
        return match(mixingMap, items);
    }

    private Pair<Food, Integer> match(Map<String, String> map, List<ItemStack> items) {
        List<String> ingredients = new ArrayList<>();
        for (ItemStack itemStack : items) {
            ingredients.add(itemStack.getType().name());
        }
        String foodName = map.get(BasicUtil.listToStringKey(ingredients));
        if (Objects.nonNull(foodName)) {
            String[] split = foodName.split(":");
            if (split.length >= 2) {
                Pair<Food, Integer> pair = new Pair<>(getFood(split[0]), Integer.parseInt(split[1]));
                if (Objects.nonNull(pair.getFirst()) && Objects.nonNull(pair.getSecond())) {
                    return pair;
                }
            }
        }
        return null;
    }

}
