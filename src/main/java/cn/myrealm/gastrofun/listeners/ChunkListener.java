package cn.myrealm.gastrofun.listeners;

import cn.myrealm.gastrofun.managers.mechanics.ChunkManager;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * @author rzt1020
 */
public class ChunkListener extends BaseListener {
    private final Map<Player, Set<Chunk>> playerChunkMap;

    public ChunkListener(JavaPlugin plugin) {
        super(plugin);
        playerChunkMap = new HashMap<>();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            playerChunkMap.put(player, getChunks(player.getLocation()));
        }
        BukkitRunnable chunkEnumerate = new BukkitRunnable() {
            @Override
            public void run() {
                Set<Chunk> chunks = new HashSet<>();
                for (Set<Chunk> chunk : playerChunkMap.values()) {
                    chunks.addAll(chunk);
                }
                ChunkManager.getInstance().updateChunks(chunks);
            }
        };
        chunkEnumerate.runTaskTimer(plugin, 10L, 20L);
    }

    public Set<Chunk> getChunks(Location location) {
        return Set.of(
             location.clone().add(8, 0, 8).getChunk(),
             location.clone().add(8, 0, -8).getChunk(),
             location.clone().add(-8, 0, 8).getChunk(),
             location.clone().add(-8, 0, -8).getChunk()
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerChunkMap.put(event.getPlayer(), getChunks(event.getPlayer().getLocation()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerChunkMap.remove(event.getPlayer());
    }


}
