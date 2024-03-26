package cn.myrealm.gastrofun.commands.subcommands;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.enums.systems.Messages;
import cn.myrealm.gastrofun.enums.mechanics.Permissions;
import cn.myrealm.gastrofun.managers.mechanics.FoodManager;
import cn.myrealm.gastrofun.mechanics.foods.Food;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rzt1020
 */
public class GiveCommand implements SubCommand {

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return Messages.COMMAND_GIVE.getMessage();
    }

    @Override
    public String getUsage() {
        return "/gastrofun give <default|food> <player> <itemName>";
    }

    @Override
    public List<String> getSubCommandAliases() {
        return new ArrayList<>();
    }


    private final static String DEFAULT = "default",
                                FOOD = "food";
    @Override
    public List<String> onTabComplete(int argsNum, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (argsNum == FIRST_ARGUMENT) {
            suggestions = List.of(DEFAULT, FOOD);
        } else if (argsNum == SECOND_ARGUMENT) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                suggestions.add(player.getName());
            }
        } else if (argsNum == THIRD_ARGUMENT) {
            if (DEFAULT.equals(args[FIRST_ARGUMENT])) {
                suggestions.addAll(DefaultItems.getNames());
            } else if (FOOD.equals(args[FIRST_ARGUMENT])) {
                FoodManager foodManager = FoodManager.getInstance();
                suggestions.addAll(foodManager.getNames());
            }
        } else if (argsNum == FOURTH_ARGUMENT) {
            if (DEFAULT.equals(args[FIRST_ARGUMENT])) {
                suggestions = List.of("[amount]");
            } else if (FOOD.equals(args[FIRST_ARGUMENT])) {
                suggestions = List.of("[recipe_id]");
            }
        } else if (argsNum == FIFTH_ARGUMENT) {
            if (FOOD.equals(args[FIRST_ARGUMENT])) {
                suggestions = List.of("[amount]");
            }
        }
        return suggestions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (!Permissions.COMMAND_GIVE.hasPermission(sender)) {
            return;
        }

        switch (args[FIRST_ARGUMENT]) {
            case DEFAULT -> handleDefault(args);
            case FOOD -> handleFood(args);
            default -> throw new Exception("Unknown item type: " + args[FIRST_ARGUMENT]);
        }
    }

    private void handleFood(String[] args) {
        Player player = Bukkit.getPlayer(args[SECOND_ARGUMENT]);
        if (Objects.nonNull(player)) {
            Food food = FoodManager.getInstance().getFood(args[THIRD_ARGUMENT]);
            ItemStack itemStack = food.getFood(Integer.parseInt(args[FOURTH_ARGUMENT]));
            if (args.length == FIVE_ARGUMENTS) {
                itemStack.setAmount(Integer.parseInt(args[FIFTH_ARGUMENT]));
            }
            player.getInventory().addItem(itemStack);
        }
    }

    private void handleDefault(String[] args) {
        Player player = Bukkit.getPlayer(args[SECOND_ARGUMENT]);
        if (Objects.nonNull(player)) {
            ItemStack itemStack = Objects.requireNonNull(DefaultItems.getByName(args[THIRD_ARGUMENT])).generateItemStack();
            if (args.length == FOUR_ARGUMENTS) {
                itemStack.setAmount(Integer.parseInt(args[FOURTH_ARGUMENT]));
            }
            player.getInventory().addItem(itemStack);
        }
    }
}
