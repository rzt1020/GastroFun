package cn.myrealm.gastrofun.listeners;


import cn.myrealm.gastrofun.mechanics.items.placeable.items.BasePlaceableItem;
import cn.myrealm.gastrofun.mechanics.scheduler.processes.RemoveScheduler;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author rzt1020
 */
public class DigListener extends BaseListener {
    private final RemoveScheduler removeScheduler;
    private final BasePlaceableItem placeableItem;
    private final Location location;

    public DigListener(JavaPlugin plugin, RemoveScheduler removeScheduler, BasePlaceableItem placeableItem, Location location) {
        super(plugin, PacketType.Play.Client.BLOCK_DIG);
        this.removeScheduler = removeScheduler;
        this.placeableItem = placeableItem;
        this.location = location;
    }

    @Override
    public void registerProtocolListener() {
        super.registerProtocolListener();
        new BukkitRunnable() {
            @Override
            public void run() {
                unRegisterProtocolListener();
            }
        }.runTaskLater(plugin, 600L);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if (event.getPacketType().equals(PacketType.Play.Client.BLOCK_DIG)) {
            removeScheduler.setRemove(false);
            placeableItem.giveUpRemove(location);
        }
    }
}
