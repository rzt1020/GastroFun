package cn.myrealm.gastrofun.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author rzt1020
 */
public interface SubCommand {
    int FIRST_ARGUMENT = 1,
        SECOND_ARGUMENT = 2,
        THIRD_ARGUMENT = 3,
        FOURTH_ARGUMENT = 4,
        FIFTH_ARGUMENT = 5;

    int NONE_ARGUMENT = 1,
        TWO_ARGUMENTS = 3,
        THREE_ARGUMENTS = 4,
        FOUR_ARGUMENTS = 5,
        FIVE_ARGUMENTS = 6;
    /**
     * get sub command name
     * @return String
     */
    String getName();

    /**
     * get sub command description
     * @return String
     */

    String getDescription();

    /**
     * get sub command usage
     * @return String
     */
    String getUsage();

    /**
     * get sub command aliases
     * @return List<String>
     */
    List<String> getSubCommandAliases();

    /**
     * get tab complete of sub command
     *
     * @param argsNum size of args
     * @param args arguments
     * @return List<String>
     */
    List<String> onTabComplete(int argsNum, String[] args);

    /**
     * execute sub command
     * @param sender command sender
     * @param args arguments
     * @throws Exception error
     */
    void execute(CommandSender sender, String[] args) throws Exception;
}
