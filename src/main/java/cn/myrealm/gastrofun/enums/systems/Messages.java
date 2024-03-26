package cn.myrealm.gastrofun.enums.systems;

import cn.myrealm.gastrofun.managers.system.LanguageManager;

import static cn.myrealm.gastrofun.GastroFun.plugin;

/**
 * @author rzt1020
 */

public enum Messages {
    // prefix
    PREFIX("prefix"),
    // information
    ENABLE_MESSAGE("info.enable-message"),
    RELOAD_SUCCESS("info.reload-success"),
    TEXTURE_PACK_CREATED("info.texture-pack-created"),
    // errors
    ERROR_INCORRECT_COMMAND("error.incorrect-command"),
    ERROR_EXECUTING_COMMAND("error.executing-command"),
    ERROR_FAILED_TO_CREATE_TEXTURE_PACK("error.failed-to-create-texture-pack"),
    // command descriptions
    COMMAND_HELP("command.help"),
    COMMAND_RELOAD("command.reload"),
    COMMAND_GIVE("command.give"),
    // command help messages
    COMMAND_HELP_HEAD("command.help-head"),
    COMMAND_HELP_DETAIL("command.help-detail"),
    // game
    GAME_DESCRIPTION("game.description"),
    GAME_FOOD_POINT("game.food-point");

    private final String key;
    Messages(String key) {
        this.key = key;
    }

    public String getMessage(String... args) {
        LanguageManager languageManager = LanguageManager.getInstance();
        String message;
        try {
            message = languageManager.getMessage(key);
        } catch (Exception e) {
            plugin.getLogger().warning(e.getMessage());
            message = "Missing message for key: " + key;
        }

        for (int i = 0; i < args.length; i += 2) {
            String var = "{" + args[i] + "}";
            message = message.replace(var, args[i + 1]);
        }

        return message;
    }
    public String getMessageWithPrefix(String... args) {
        if (this.equals(Messages.PREFIX)) {
            return getMessage();
        }
        return Messages.PREFIX.getMessage() + getMessage(args);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
