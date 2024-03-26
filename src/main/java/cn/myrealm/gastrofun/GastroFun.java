package cn.myrealm.gastrofun;

import cn.myrealm.gastrofun.commands.subcommands.GiveCommand;
import cn.myrealm.gastrofun.commands.subcommands.HelpCommand;
import cn.myrealm.gastrofun.commands.MainCommand;
import cn.myrealm.gastrofun.commands.subcommands.ReloadCommand;
import cn.myrealm.gastrofun.enums.systems.Messages;
import cn.myrealm.gastrofun.listeners.*;
import cn.myrealm.gastrofun.managers.BaseManager;
import cn.myrealm.gastrofun.managers.mechanics.ChunkManager;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.managers.system.DatabaseManager;
import cn.myrealm.gastrofun.managers.system.LanguageManager;
import cn.myrealm.gastrofun.managers.system.TextureManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author rzt1020
 */
public final class GastroFun extends JavaPlugin {
    public static GastroFun plugin;
    public static ProtocolManager protocolManager;
    private final List<BaseManager> managers = new ArrayList<>();
    public static final Random RANDOM = new Random();

    @Override
    public void onEnable() {
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();

        outputDefaultFiles();

        initPlugin();
        registerDefaultCommands();
        registerDefaultListeners();

        Bukkit.getConsoleSender().sendMessage(Messages.ENABLE_MESSAGE.getMessageWithPrefix());
    }

    @Override
    public void onDisable() {
        for (BaseManager manager : managers) {
            manager.disable();
        }
    }

    public void initPlugin() {
        managers.clear();
        managers.add(new DatabaseManager(this));
        managers.add(new LanguageManager(this));
        managers.add(new TextureManager(this));
        managers.add(new PlaceableItemManager(this));
        managers.add(new FoodManager(this));
        managers.add(new ChunkManager(this));
    }

    public void registerDefaultCommands() {
        MainCommand command = new MainCommand();

        command.registerSubCommand(new HelpCommand());
        command.registerSubCommand(new ReloadCommand());
        command.registerSubCommand(new GiveCommand());

        //noinspection ConstantConditions
        getCommand("gastrofun").setExecutor(command);
        //noinspection ConstantConditions
        getCommand("gastrofun").setTabCompleter(command);
    }
    public void registerDefaultListeners() {
        new FoodListener(this).registerBukkitListener();
        new PlaceListener(this).registerBukkitListener();
        new ChunkListener(this).registerBukkitListener();
        new RemoveListener(this).registerBukkitListener();
        new TriggerListener(this).registerBukkitListener();
    }

    static final List<String> FILES = Arrays.asList(
            "foods/meat_skewer.yml",
            "placeable_foods/chocolate_pie.yml",
            "languages/en_US.yml",
            "pack/assets/gastrofun/models/block/skillet.json",
            "pack/assets/gastrofun/models/block/mixing_bowl.json",
            "pack/assets/gastrofun/models/block/mixing_spoon.json",
            "pack/assets/gastrofun/models/block/grill.json",
            "pack/assets/gastrofun/textures/block/skillet.png",
            "pack/assets/gastrofun/textures/block/mixing_bowl.png",
            "pack/assets/gastrofun/textures/block/mixing_spoon.png",
            "pack/assets/gastrofun/textures/block/handle-1.png",
            "pack/assets/gastrofun/textures/block/handle-2.png",
            "pack/assets/gastrofun/textures/block/a1.png",
            "pack/assets/gastrofun/textures/block/a2.png",
            "pack/assets/gastrofun/textures/block/a3.png",
            "pack/assets/minecraft/font/default.json",
            "pack/assets/minecraft/models/item/red_dye.json",
            "pack/pack.png",
            "pack/pack.mcmeta",
            "textures/foods/meat_skewer.png",
            "textures/placeable_foods/models/chocolate_pie/chocolate_pie_0.json",
            "textures/placeable_foods/models/chocolate_pie/chocolate_pie_1.json",
            "textures/placeable_foods/models/chocolate_pie/chocolate_pie_2.json",
            "textures/placeable_foods/models/chocolate_pie/chocolate_pie_3.json",
            "textures/placeable_foods/textures/chocolate_pie/choco_pie.png",
            "textures/placeable_foods/chocolate_pie.png",
            "config.yml");

    public void outputDefaultFiles() {
        FILES.forEach(file -> {
            try {
                if (!Files.exists(Paths.get(getDataFolder().getPath() + "/" + file))) {
                    saveResource(file, false);
                }
            } catch (Exception e) {
                String[] names = file.split("/");
                Bukkit.getConsoleSender().sendMessage("§x§9§8§F§B§9§8[GastroFun] §fCan not output default config file: " + names[names.length - 1]);
            }
        });
    }

    public void reloadPlugin() {
        disablePlugin();
        initPlugin();
    }

    public void disablePlugin() {
        for (BaseManager manager : managers) {
            manager.disable();
        }
    }
}
