package cn.myrealm.gastrofun.managers.mechanics;


import cn.myrealm.gastrofun.managers.BaseManager;
import cn.myrealm.gastrofun.mechanics.misc.PlaceableChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rzt1020
 */
public class ChunkManager extends BaseManager {
    private static ChunkManager instance;
    private Map<String, PlaceableChunk> chunksMap;

    public ChunkManager(JavaPlugin plugin) {
        super(plugin);
        instance = this;
    }
    public static ChunkManager getInstance() {
        return instance;
    }

    @Override
    protected void onInit() {
        chunksMap = new HashMap<>(5);
    }

    @Override
    protected void onDisable() {
        for (PlaceableChunk chunk : chunksMap.values()) {
            chunk.forceUnloadChunk();
        }
        chunksMap.clear();
    }

    public void updateChunks(Set<Chunk> chunks) {
        Set<String> oldChunks = new HashSet<>(chunksMap.keySet());
        Set<String> newChunks = chunks.stream()
                .map(chunk -> String.format("%s,%d,%d",
                        chunk.getWorld().getName(),
                        chunk.getX(),
                        chunk.getZ()))
                .collect(Collectors.toSet());
        oldChunks.removeAll(newChunks);
        newChunks.addAll(oldChunks);

        for (String chunk : newChunks) {
            if (chunksMap.containsKey(chunk)) {
                chunksMap.get(chunk).display();
            } else {
                chunksMap.put(chunk, new PlaceableChunk(chunk));
            }
        }

        for (String chunk : oldChunks) {
            if (chunksMap.get(chunk).unloadChunk()) {
                chunksMap.remove(chunk);
            }
        }
    }

    public PlaceableChunk getChunk(Chunk chunk) {
        return chunksMap.get(String.format("%s,%d,%d",
                chunk.getWorld().getName(),
                chunk.getX(),
                chunk.getZ()));
    }

}
