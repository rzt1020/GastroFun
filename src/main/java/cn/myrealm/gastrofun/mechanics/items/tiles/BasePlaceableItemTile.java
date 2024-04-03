package cn.myrealm.gastrofun.mechanics.items.tiles;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.managers.mechanics.ChunkManager;
import cn.myrealm.gastrofun.mechanics.items.Placeable;
import cn.myrealm.gastrofun.mechanics.scheduler.animations.TryRemoveScheduler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;

import java.util.*;

/**
 * @author rzt1020
 */
public abstract class BasePlaceableItemTile implements Placeable {
    protected final int entityId;
    protected Location location;
    protected final String placeableName;
    private TryRemoveScheduler tryRemoveScheduler;
    protected int state = 0;
    protected List<Player> players = new ArrayList<>();

    public BasePlaceableItemTile(String placeableName) {
        entityId = Math.abs(GastroFun.RANDOM.nextInt());
        this.placeableName = placeableName;
    }
    public BasePlaceableItemTile(int entityId, String placeableName) {
        this.entityId = entityId;
        this.placeableName = placeableName;
    }

    @Override
    public void place(Location location, Quaternionf rotation, int state, ItemStack itemStack) {
        ChunkManager.getInstance().getChunk(location.getChunk()).writePlaceableItem(entityId, placeableName, location, rotation, state);
        display(location, rotation, state);
    }

    @Override
    public void display(Location location, Quaternionf rotation, int state) {
        this.location = location;
        this.state = state;
        sendEntityPacket(location, rotation, state);
    }


    /**
     * send packet to nearby players
     * @param location entity location
     * @param rotation entity rotation
     * @param state entity state
     */
    public abstract void sendEntityPacket(Location location, Quaternionf rotation, int state);

    @Override
    public void remove(Location location) {
        if (Objects.nonNull(tryRemoveScheduler)) {
            giveUpRemove();
        }
        ChunkManager.getInstance().getChunk(location.getChunk()).removePlaceableItem(entityId);
        hide(location);
    }

    @Override
    public void hide(Location location) {
        removeEntityPacket(location);
    }


    /**
     * send remove packet to nearby players
     * @param location entity location
     */
    public abstract void removeEntityPacket(Location location);

    public void tryRemove() {
        tryRemoveScheduler = new TryRemoveScheduler(GastroFun.plugin, 1, -1, entityId, players.stream().toList());
        tryRemoveScheduler.play(0);
    }

    public void giveUpRemove() {
        if (Objects.nonNull(tryRemoveScheduler)) {
            tryRemoveScheduler.end();
            tryRemoveScheduler = null;
        }
    }

    /**
     * if function working
     * @return is function working
     */
    public abstract boolean isFunctioning();
}
