package cn.myrealm.gastrofun.enums.systems;

/**
 * @author rzt1020
 */

public enum SQLs {
    // create tables
    CREATE_FOOD_TABLE(
            "CREATE TABLE IF NOT EXISTS gastrofun_food (food_name VARCHAR(255) NOT NULL, custommodeldata INT NOT NULL, PRIMARY KEY (food_name), UNIQUE (custommodeldata));",
            "CREATE TABLE IF NOT EXISTS gastrofun_food (food_name TEXT NOT NULL, custommodeldata INTEGER NOT NULL, PRIMARY KEY (food_name), UNIQUE (custommodeldata));"
    ),
    CREATE_PLACEABLE_FOOD_TABLE(
            "CREATE TABLE IF NOT EXISTS gastrofun_placeable_food (food_name VARCHAR(255) NOT NULL, custommodeldata INT NOT NULL, PRIMARY KEY (food_name), UNIQUE (custommodeldata));",
            "CREATE TABLE IF NOT EXISTS gastrofun_placeable_food (food_name TEXT NOT NULL, custommodeldata INTEGER NOT NULL, PRIMARY KEY (food_name), UNIQUE (custommodeldata));"
    ),
    // queries
    QUERY_FOOD_TABLE(
            "SELECT * FROM gastrofun_food;"
    ),
    QUERY_PLACEABLE_FOOD_TABLE(
            "SELECT * FROM gastrofun_placeable_food;"
    ),
    INSERT_FOOD_TABLE(
            "INSERT INTO gastrofun_food (food_name, custommodeldata) VALUES ('{0}', {1});"
    ),
    INSERT_PLACEABLE_FOOD_TABLE(
            "INSERT INTO gastrofun_placeable_food (food_name, custommodeldata) VALUES ('{0}', {1});"
    );

    private final String mysql, sqlite;
    SQLs(String mysql, String sqlite) {
        this.mysql = mysql;
        this.sqlite = sqlite;
    }
    SQLs(String mysql) {
        this.mysql = mysql;
        this.sqlite = mysql;
    }

    public String getSql(String... args) {
        String sql;
        if (Config.USE_MYSQL.asBoolean()) {
            sql = mysql;
        } else {
            sql = sqlite;
        }
        for (int i = 0; i < args.length; i++) {
            sql = sql.replace("{" + i + "}", args[i]);
        }
        return sql;
    }
}
