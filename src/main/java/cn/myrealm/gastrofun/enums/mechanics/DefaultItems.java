package cn.myrealm.gastrofun.enums.mechanics;

import cn.myrealm.gastrofun.mechanics.items.DefaultItem;
import cn.myrealm.gastrofun.mechanics.items.MixingSpoon;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.Grill;
import cn.myrealm.gastrofun.mechanics.items.Placeable;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.MixingBowl;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.Skillet;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author rzt1020
 */

public enum DefaultItems {
    // items
    SKILLET("skillet", 10000, Skillet::new, true),
    GRILL("grill", 10001, Grill::new, true),
    MIXING_BOWL("mixing_bowl", 10002, MixingBowl::new, true),
    MIXING_SPOON("mixing_spoon", 10003, MixingSpoon::new, false);
    private final String name;
    private final int customModelData;
    private final Supplier<DefaultItem> itemSupplier;
    private final boolean accessible;
    DefaultItems(String name, int customModelData, Supplier<DefaultItem> itemSupplier, boolean accessible) {
        this.name = name;
        this.customModelData = customModelData;
        this.itemSupplier = itemSupplier;
        this.accessible = accessible;
    }

    public String getName() {
        return name;
    }
    public int getCustomModelData() {
        return customModelData;
    }

    public ItemStack generateItemStack() {
        return itemSupplier.get().generate();
    }

    public boolean isPlaceable() {
        return Placeable.class.isAssignableFrom(itemSupplier.get().getClass());
    }

    public static DefaultItems getByName(String name) {
        for (DefaultItems defaultItem : DefaultItems.values()) {
            if (defaultItem.getName().equalsIgnoreCase(name)) {
                return defaultItem;
            }
        }
        return null;
    }
    public static List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (DefaultItems defaultItem : DefaultItems.values()) {
            if (defaultItem.accessible) {
                names.add(defaultItem.getName());
            }
        }
        return names;
    }
}
