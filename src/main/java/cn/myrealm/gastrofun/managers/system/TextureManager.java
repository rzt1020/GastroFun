package cn.myrealm.gastrofun.managers.system;


import cn.myrealm.gastrofun.enums.systems.Messages;
import cn.myrealm.gastrofun.enums.systems.SQLs;
import cn.myrealm.gastrofun.managers.BaseManager;
import cn.myrealm.gastrofun.utils.BasicUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author rzt1020
 */
public class TextureManager extends BaseManager {

    private static TextureManager instance;
    private Map<String, Integer> foodCustomModelDataMap;
    private Map<String, Integer> placeableFoodCustomModelDataMap;
    private Map<String, List<String>> placeableFoodModelMap;
    private List<Integer> foodCustomModelDataList;
    private List<Integer> placeableFoodCustomModelDataList;
    private List<String> placeableFoodModelList;

    public TextureManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }

    public static TextureManager getInstance() {
        return instance;
    }

    @Override
    protected void onInit() {
        foodCustomModelDataMap = new HashMap<>(5);
        foodCustomModelDataList = new ArrayList<>();
        placeableFoodCustomModelDataMap = new HashMap<>(5);
        placeableFoodCustomModelDataList = new ArrayList<>();
        placeableFoodModelMap = new HashMap<>(5);
        placeableFoodModelList = new ArrayList<>();

        DatabaseManager.getInstance().executeAsyncQuery(SQLs.QUERY_FOOD_TABLE.getSql(), new DatabaseManager.Callback<>() {
            @Override
            public void onSuccess(List<Map<String, Object>> results) {
                for (Map<String, Object> result : results) {
                    Object foodName = result.get("food_name"), custommodeldata = result.get("custommodeldata");
                    if (Objects.nonNull(foodName) && Objects.nonNull(custommodeldata)) {
                        foodCustomModelDataMap.put((String) foodName, (Integer) custommodeldata);
                        foodCustomModelDataList.add((Integer) custommodeldata);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        DatabaseManager.getInstance().executeAsyncQuery(SQLs.QUERY_PLACEABLE_FOOD_TABLE.getSql(), new DatabaseManager.Callback<>() {
            @Override
            public void onSuccess(List<Map<String, Object>> results) {
                for (Map<String, Object> result : results) {
                    Object placeableFoodName = result.get("food_name"), custommodeldata = result.get("custommodeldata");
                    if (Objects.nonNull(placeableFoodName) && Objects.nonNull(custommodeldata)) {
                        placeableFoodCustomModelDataMap.put((String) placeableFoodName, (Integer) custommodeldata);
                        placeableFoodCustomModelDataList.add((Integer) custommodeldata);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, ()-> {
            loadTextures();
            outputTextures();

            Bukkit.getConsoleSender().sendMessage(Messages.TEXTURE_PACK_CREATED.getMessageWithPrefix());
        }, 40);
    }

    public int getFoodCustomModelData(String foodName) {
        if (foodCustomModelDataMap.containsKey(foodName)) {
            return foodCustomModelDataMap.get(foodName);
        }
        return placeableFoodCustomModelDataMap.getOrDefault(foodName, 0);
    }
    public int getPlaceableFoodCustomModelData(String foodName) {
        return placeableFoodCustomModelDataMap.get(foodName);
    }


    private void loadTextures() {
        loadFoodTextures();
        loadPlaceableFoodTextures();
    }

    private void loadFoodTextures() {
        if (!new File(Path.FOOD_TEXTURE_PATH.toString()).exists() && new File(Path.FOOD_TEXTURE_PATH.toString()).mkdirs()) {
            return;
        }
        File[] foodTextureFiles = new File(Path.FOOD_TEXTURE_PATH.toString()).listFiles();
        if (Objects.isNull(foodTextureFiles)) {
            return;
        }
        for (File foodTextureFile : foodTextureFiles) {
            if (foodTextureFile.getName().endsWith(".png")) {
                String foodName = foodTextureFile.getName().replace(".png", "");
                if (! foodCustomModelDataMap.containsKey(foodName)) {
                    int custommodeldata = 10000;
                    while (foodCustomModelDataList.contains(custommodeldata)) {
                        custommodeldata ++;
                    }
                    String sql = SQLs.INSERT_FOOD_TABLE.getSql(foodName, String.valueOf(custommodeldata));
                    DatabaseManager.getInstance().executeAsyncUpdate(sql);
                    foodCustomModelDataMap.put(foodName, custommodeldata);
                    foodCustomModelDataList.add(custommodeldata);
                }
            }
        }
    }
    private void loadPlaceableFoodTextures() {
        if (!new File(Path.PLACEABLE_FOOD_TEXTURE_PATH.toString()).exists() && new File(Path.PLACEABLE_FOOD_TEXTURE_PATH.toString()).mkdirs()) {
            return;
        }
        File[] placeableFoodTextureFiles = new File(Path.PLACEABLE_FOOD_TEXTURE_PATH.toString()).listFiles();
        if (Objects.isNull(placeableFoodTextureFiles)) {
            return;
        }
        for (File placeableFoodTextureFile : placeableFoodTextureFiles) {
            if (placeableFoodTextureFile.getName().endsWith(".png")) {
                String foodName = placeableFoodTextureFile.getName().replace(".png", "");
                if (! placeableFoodCustomModelDataMap.containsKey(foodName)) {
                    int custommodeldata = 10000;
                    while (placeableFoodCustomModelDataList.contains(custommodeldata)) {
                        custommodeldata ++;
                    }
                    String sql = SQLs.INSERT_PLACEABLE_FOOD_TABLE.getSql(foodName, String.valueOf(custommodeldata));
                    DatabaseManager.getInstance().executeAsyncUpdate(sql);
                    placeableFoodCustomModelDataMap.put(foodName, custommodeldata);
                    placeableFoodCustomModelDataList.add(custommodeldata);
                    placeableFoodModelList.add(foodName);
                }
                loadPlaceableFoodModels(foodName);
            }
        }
    }
    private void loadPlaceableFoodModels(String foodName) {
        List<String> modelList = new ArrayList<>();
        int i = 0;
        while (new File(Path.PLACEABLE_FOOD_MODEL_PATH.toString(foodName, String.valueOf(i))).exists()) {
            File file =  new File(Path.PLACEABLE_FOOD_MODEL_PATH.toString(foodName, String.valueOf(i)));
            String placeableFoodModelName = file.getName().replace(".json", "");
            modelList.add(placeableFoodModelName);
            if (! placeableFoodCustomModelDataMap.containsKey(placeableFoodModelName)) {
                int custommodeldata = 10000;
                while (placeableFoodCustomModelDataList.contains(custommodeldata)) {
                    custommodeldata ++;
                }
                String sql = SQLs.INSERT_PLACEABLE_FOOD_TABLE.getSql(placeableFoodModelName, String.valueOf(custommodeldata));
                DatabaseManager.getInstance().executeAsyncUpdate(sql);
                placeableFoodCustomModelDataMap.put(placeableFoodModelName, custommodeldata);
                placeableFoodCustomModelDataList.add(custommodeldata);
            }
            i++;
        }
        if (!modelList.isEmpty()) {
            placeableFoodModelMap.put(foodName, modelList);
        }
    }
    private void outputTextures() {
        outputFoodTextures();
        outputPlaceableFoodTextures();
    }
    private void outputFoodTextures(){
        boolean created;
        created = (new File(Path.PACK_PATH.toString()).exists() || mkdirs(Path.PACK_PATH.toString()));
        created = created && (new File(Path.PACK_MAIN_MODEL_PATH.toString()).exists() || mkdirs(Path.PACK_MAIN_MODEL_PATH.toString()));
        created = created && (new File(Path.PACK_ITEM_MODEL_PATH.toString()).exists() || mkdirs(Path.PACK_ITEM_MODEL_PATH.toString()));
        created = created && (new File(Path.PACK_ITEM_TEXTURE_PATH.toString()).exists() || mkdirs(Path.PACK_ITEM_TEXTURE_PATH.toString()));

        if (created) {
            Map<Integer, String> overrides = new HashMap<>(5);
            for (String foodName : foodCustomModelDataMap.keySet()) {
                File pic = new File(Path.FOOD_TEXTURE_PATH.toString() , foodName + ".png");
                if (pic.exists()) {
                    try {
                        Files.copy(pic.toPath(), new File(Path.PACK_ITEM_TEXTURE_PATH.toString(), foodName + ".png").toPath(),  StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String itemModel = Template.ITEM_MODEL_TEMPLATE.toString().replace("%item_name%", foodName);
                    try {
                        Files.write(new File(Path.PACK_ITEM_MODEL_PATH.toString(), foodName + ".json").toPath(), itemModel.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    overrides.put(foodCustomModelDataMap.get(foodName), Template.OVERRIDE_ITEM_TEMPLATE.toString().replace("%item_name%", foodName).replace("%custommodeldata%", String.valueOf(foodCustomModelDataMap.get(foodName))));
                }
            }
            StringBuilder override = new StringBuilder();
            int i = 10000;
            while (!overrides.isEmpty()) {
                while (!overrides.containsKey(i)) {
                    i++;
                }
                override.append(overrides.get(i));
                overrides.remove(i);

                if (!overrides.isEmpty()) {
                    override.append(", ");
                }
            }
            try {
                Files.write(new File(Path.PACK_MAIN_MODEL_PATH.toString(), "melon_slice.json").toPath(), Template.MAIN_ITEM_MODEL_TEMPLATE.toString().replace("%item_name%", "melon_slice").replace("%overrides%", override.toString()).getBytes());
                Files.write(new File(Path.PACK_MAIN_MODEL_PATH.toString(), "mushroom_stew.json").toPath(), Template.MAIN_ITEM_MODEL_TEMPLATE.toString().replace("%item_name%", "mushroom_stew").replace("%overrides%", override.toString()).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void outputPlaceableFoodTextures() {
        boolean created;
        created = (new File(Path.PACK_PATH.toString()).exists() || mkdirs(Path.PACK_PATH.toString()));
        created = created && (new File(Path.PACK_MAIN_MODEL_PATH.toString()).exists() || mkdirs(Path.PACK_MAIN_MODEL_PATH.toString()));
        created = created && (new File(Path.PACK_ITEM_MODEL_PATH.toString()).exists() || mkdirs(Path.PACK_ITEM_MODEL_PATH.toString()));
        created = created && (new File(Path.PACK_ITEM_TEXTURE_PATH.toString()).exists() || mkdirs(Path.PACK_ITEM_TEXTURE_PATH.toString()));

        if (created) {
            Map<Integer, String> overrides = new HashMap<>(5);
            for (String foodName : placeableFoodModelMap.keySet()) {
                File pic = new File(Path.PLACEABLE_FOOD_TEXTURE_PATH.toString() , foodName + ".png");
                if (pic.exists()) {
                    try {
                        Files.copy(pic.toPath(), new File(Path.PACK_ITEM_TEXTURE_PATH.toString(), foodName + ".png").toPath(),  StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String itemModel = Template.ITEM_MODEL_TEMPLATE.toString().replace("%item_name%", foodName);
                    try {
                        Files.write(new File(Path.PACK_ITEM_MODEL_PATH.toString(), foodName + ".json").toPath(), itemModel.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    overrides.put(placeableFoodCustomModelDataMap.get(foodName), Template.OVERRIDE_ITEM_TEMPLATE.toString().replace("%item_name%", foodName).replace("%custommodeldata%", String.valueOf(placeableFoodCustomModelDataMap.get(foodName))));
                    for (String placeableFoodModelName : placeableFoodModelMap.get(foodName)) {
                        int customModelData = placeableFoodCustomModelDataMap.get(placeableFoodModelName);
                        overrides.put(customModelData, Template.OVERRIDE_PLACEABLE_FOOD_MODEL_TEMPLATE.toString().replace("%placeable_food_name%", foodName).replace("%custommodeldata%", String.valueOf(customModelData)).replace("%placeable_food_model_name%", placeableFoodModelName));
                    }
                }
            }
            StringBuilder override = new StringBuilder();
            int i = 10000;
            while (!overrides.isEmpty()) {
                while (!overrides.containsKey(i)) {
                    i++;
                }
                override.append(overrides.get(i));
                overrides.remove(i);

                if (!overrides.isEmpty()) {
                    override.append(", ");
                }
            }
            try {
                Files.write(new File(Path.PACK_MAIN_MODEL_PATH.toString(), "yellow_dye.json").toPath(), Template.MAIN_ITEM_MODEL_TEMPLATE.toString().replace("%item_name%", "yellow_dye").replace("%overrides%", override.toString()).getBytes());
                BasicUtil.copyDirectory(new File(Path.PLACEABLE_FOOD_TEXTURE_DIR.toString()), new File(Path.PACK_TEXTURE_DIR.toString()));
                BasicUtil.copyDirectory(new File(Path.PLACEABLE_FOOD_MODEL_DIR.toString()), new File(Path.PACK_MODEL_DIR.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    public static boolean mkdirs(String path) {
        Stack<File> stack = new Stack<>();
        stack.push(new File(path));
        while (! stack.peek().exists()) {
            stack.push(stack.peek().getParentFile());
        }
        stack.pop();
        boolean success = true;
        while (! stack.isEmpty()) {
            success = success && stack.pop().mkdirs();
        }
        return success;
    }


    enum Path {
        // packs
        PACK_PATH("plugins/GastroFun/pack/"),
        PACK_MAIN_MODEL_PATH(PACK_PATH + "assets/minecraft/models/item/"),
        PACK_ITEM_MODEL_PATH(PACK_PATH + "assets/gastrofun/models/item/"),
        PACK_ITEM_TEXTURE_PATH(PACK_PATH + "assets/gastrofun/textures/item/"),
        // texture
        FOOD_TEXTURE_PATH("plugins/GastroFun/textures/foods/"),
        PLACEABLE_FOOD_TEXTURE_PATH("plugins/GastroFun/textures/placeable_foods/"),
        PLACEABLE_FOOD_MODEL_PATH("plugins/GastroFun/textures/placeable_foods/models/{0}/{0}_{1}.json"),
        PLACEABLE_FOOD_MODEL_DIR("plugins/GastroFun/textures/placeable_foods/models/"),
        PLACEABLE_FOOD_TEXTURE_DIR("plugins/GastroFun/textures/placeable_foods/textures/"),
        PACK_MODEL_DIR("plugins/GastroFun/pack/assets/gastrofun/models/block/"),
        PACK_TEXTURE_DIR("plugins/GastroFun/pack/assets/gastrofun/textures/block/");

        private final String path;
        Path(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return path;
        }

        public String toString(String... args) {
            String path = this.path;
            for (int i = 0; i < args.length; i++) {
                path = path.replace("{" + i + "}", args[i]);
            }
            return path;
        }
    }

    enum Template{
        // item templates
        ITEM_MODEL_TEMPLATE("{\"parent\":\"item/handheld\",\"textures\":{\"layer0\":\"gastrofun:item/%item_name%\"}}"),
        MAIN_ITEM_MODEL_TEMPLATE("{\"parent\":\"minecraft:item/generated\",\"textures\":{\"layer0\":\"minecraft:item/%item_name%\"},\"overrides\":[%overrides%]}"),
        OVERRIDE_ITEM_TEMPLATE("{\"predicate\":{\"custom_model_data\":%custommodeldata%},\"model\":\"gastrofun:item/%item_name%\"}"),
        OVERRIDE_PLACEABLE_FOOD_MODEL_TEMPLATE("{\"predicate\":{\"custom_model_data\":%custommodeldata%},\"model\":\"gastrofun:block/%placeable_food_name%/%placeable_food_model_name%\"}");

        private final String template;
        Template(String template) {
            this.template = template;
        }

        @Override
        public String toString() {
            return template;
        }
    }
}
