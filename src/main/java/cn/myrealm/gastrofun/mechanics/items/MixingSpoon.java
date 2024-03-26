package cn.myrealm.gastrofun.mechanics.items;


import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author rzt1020
 */
public class MixingSpoon implements DefaultItem {
    @Override
    public ItemStack generate() {
        return ItemUtil.generateItemStack(Material.RED_DYE, DefaultItems.MIXING_SPOON.getCustomModelData(), null, null);
    }
}
