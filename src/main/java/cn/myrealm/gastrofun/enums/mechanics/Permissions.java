package cn.myrealm.gastrofun.enums.mechanics;

import org.bukkit.command.CommandSender;

/**
 * @author rzt1020
 */

public enum Permissions {
    // root
    ROOT("gastrofun."),
    // commands
    COMMAND(ROOT + "command."),
    COMMAND_HELP(COMMAND + "help"),
    COMMAND_RELOAD(COMMAND + "reload"),
    COMMAND_GIVE(COMMAND + "give"),
    // plays
    PLAY(ROOT + "play.");


    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return permission;
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(this.permission);
    }
}
