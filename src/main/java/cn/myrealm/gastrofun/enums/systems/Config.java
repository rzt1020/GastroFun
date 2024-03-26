package cn.myrealm.gastrofun.enums.systems;

import cn.myrealm.gastrofun.GastroFun;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author rzt1020
 */

public enum Config {
    // database
    CONFIG_FILES_LANGUAGE("config_files.language", "en_US"),
    USE_MYSQL("database.use_mysql", false),
    MYSQL_HOST("database.mysql.host", "localhost"),
    MYSQL_PORT("database.mysql.port", 3306),
    MYSQL_USER("database.mysql.user", "root"),
    MYSQL_PASSWORD("database.mysql.password", "root"),
    MYSQL_DATABASE("database.mysql.database", "minecraft");

    private final String key;
    private final Object def;

    Config(String key, Object def) {
        this.key = key;
        this.def = def;
    }

    public String asString() {
        FileConfiguration config = GastroFun.plugin.getConfig();
        return config.getString(key, (String) def);
    }

    public int asInt() {
        FileConfiguration config = GastroFun.plugin.getConfig();
        return config.getInt(key, (Integer) def);
    }

    public boolean asBoolean() {
        FileConfiguration config = GastroFun.plugin.getConfig();
        return config.getBoolean(key, (Boolean) def);
    }
    public double asDouble() {
        FileConfiguration config = GastroFun.plugin.getConfig();
        return config.getDouble(key, (Double) def);
    }

}
