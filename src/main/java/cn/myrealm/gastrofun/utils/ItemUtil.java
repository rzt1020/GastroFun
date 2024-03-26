package cn.myrealm.gastrofun.utils;

import cn.myrealm.gastrofun.managers.system.LanguageManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rzt10
 */
public class ItemUtil {
    private ItemUtil() {
    }
    public static ItemStack generateItemStack(Material material, @Nullable Integer cmd, @Nullable String displayName, @Nullable List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        if (Objects.nonNull(cmd)) {
            itemMeta.setCustomModelData(cmd);
        }
        if (Objects.nonNull(displayName)) {
            itemMeta.setDisplayName(LanguageManager.parseColor("&r" + displayName));
        }
        if (Objects.nonNull(lore)) {
            itemMeta.setLore(lore.stream()
                    .map(line -> LanguageManager.parseColor("&r&f" + line))
                    .collect(Collectors.toList()));
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static void setDisplayName(ItemStack itemStack, String s) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (Objects.nonNull(itemMeta)) {
            itemMeta.setDisplayName(LanguageManager.parseColor("&r" + s));
            itemStack.setItemMeta(itemMeta);
        }
    }
}
