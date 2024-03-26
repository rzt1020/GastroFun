package cn.myrealm.gastrofun.listeners;

import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.systems.NamespacedKeys;
import cn.myrealm.gastrofun.listeners.BaseListener;
import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.items.placeable.items.BasePlaceableItem;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Quaternionf;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author rzt1020
 */
public class PlaceListener extends BaseListener {
    public PlaceListener(JavaPlugin plugin) {
        super(plugin);
    }
    private final Set<Player> playerCache = new HashSet<>();

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ) {
            return;
        }
        ItemStack itemStack = event.getItem();
        if (Objects.isNull(itemStack) || itemStack.getType().isAir()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (Objects.isNull(itemMeta) || !NamespacedKeys.PLACEABLE_NAME.has(itemMeta, PersistentDataType.STRING)) {
            return;
        }
        event.setCancelled(true);
        if (playerCache.contains(event.getPlayer())) {
            return;
        }
        playerCache.add(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                playerCache.remove(event.getPlayer());
            }
        }.runTaskLater(plugin, 5L);
        BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem((String) NamespacedKeys.PLACEABLE_NAME.get(itemMeta, PersistentDataType.STRING));
        Location location = placeableItem.getLocation(event.getClickedBlock(), event.getBlockFace());
        if (Objects.isNull(location) || Objects.nonNull(PlaceableItemManager.getInstance().getPlaceableItem(location))) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(GastroFun.plugin, () -> {
            Location playerLocation = event.getPlayer().getLocation().getBlock().getLocation();
            if (placeableItem.mustAir() && !location.getBlock().getType().isAir()) {
                return;
            }
            if (!placeableItem.canPlacedAt(location)) {
                return;
            }
            if (playerLocation.equals(location) || playerLocation.add(0, 1, 0).equals(location)) {
                return;
            }
            PacketUtil.swingItem(event.getPlayer());
            Quaternionf rotation = BasicUtil.directionToQuaternion(location.clone().add(.5,.5,.5), playerLocation);
            placeableItem.place(location, rotation, 0, itemStack);
            if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL) ||
                    event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
                consumeItem(event.getPlayer(), event.getItem());
            }
        }, 1);

    }
}
