package cn.myrealm.gastrofun;

import cn.myrealm.gastrofun.commands.MainCommand;
import cn.myrealm.gastrofun.commands.subcommands.GiveCommand;
import cn.myrealm.gastrofun.commands.subcommands.HelpCommand;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public void outputDefaultFiles() {
        getResourceList().forEach(file -> {
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

    private List<String> getResourceList() {
        List<String> lines = new ArrayList<>();
        Reader reader = getTextResource("default_files");

        if (reader != null) {
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines;
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
