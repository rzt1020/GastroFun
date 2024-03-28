package cn.myrealm.gastrofun.commands.subcommands;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.Permissions;
import cn.myrealm.gastrofun.enums.systems.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rzt1020
 */
public class ReloadCommand implements SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return Messages.COMMAND_RELOAD.getMessage();
    }

    @Override
    public String getUsage() {
        return "/gastro reload";
    }

    @Override
    public List<String> getSubCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public List<String> onTabComplete(int argsNum, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permissions.COMMAND_RELOAD.hasPermission(sender)) {
            return;
        }
        if (args.length != NONE_ARGUMENT) {
            sender.sendMessage(Messages.ERROR_INCORRECT_COMMAND.getMessageWithPrefix());
        }
        GastroFun.plugin.reloadPlugin();
        if (sender instanceof Player) {
            sender.sendMessage(Messages.RELOAD_SUCCESS.getMessageWithPrefix());
        }
        Bukkit.getConsoleSender().sendMessage(Messages.RELOAD_SUCCESS.getMessageWithPrefix());
    }
}
