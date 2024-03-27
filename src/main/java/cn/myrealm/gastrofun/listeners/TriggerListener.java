package cn.myrealm.gastrofun.listeners;


import cn.myrealm.gastrofun.managers.mechanics.PlaceableItemManager;
import cn.myrealm.gastrofun.mechanics.items.Triggerable;
import cn.myrealm.gastrofun.mechanics.items.items.BasePlaceableItem;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author rzt1020
 */
public class TriggerListener extends BaseListener {
    private final Set<Player> players;
    public TriggerListener(JavaPlugin plugin) {
        super(plugin);
        players = new HashSet<>();
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) ) {
            return;
        }
        if (players.contains(event.getPlayer())) {
            return;
        }
        Block block = event.getClickedBlock();
        if (Objects.isNull(block)) {
            return;
        }
        BasePlaceableItem placeableItem = PlaceableItemManager.getInstance().getPlaceableItem(block.getLocation());
        if (Objects.isNull(placeableItem)) {
            return;
        }
        event.setCancelled(true);
        players.add(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                players.remove(event.getPlayer());
            }
        }.runTaskLater(plugin, 5L);
        if (placeableItem instanceof Triggerable triggerable) {
            PacketUtil.swingItem(event.getPlayer());
            if (triggerable.trigger(event.getPlayer(), event.getItem(), block.getLocation())) {
                consumeItem(event.getPlayer(), Objects.requireNonNull(event.getItem()));
            }
        }
    }

    @EventHandler
    public void onThrow(PlayerEggThrowEvent event) {
        if (players.contains(event.getPlayer())) {
            event.setHatching(false);
        }
    }
}
