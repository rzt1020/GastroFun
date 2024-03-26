package cn.myrealm.gastrofun.mechanics.scheduler.animations;


import cn.myrealm.gastrofun.mechanics.scheduler.BaseScheduler;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * @author rzt1020
 */
public class LightUpEffectScheduler extends BaseScheduler {
    private final static List<BlockFace> BLOCK_FACES = List.of(
            BlockFace.UP,
            BlockFace.SOUTH,
            BlockFace.NORTH,
            BlockFace.WEST,
            BlockFace.EAST
    );
    private final Block block;
    private final long lightUpPeriod;
    private int lightLevel = 0;

    public LightUpEffectScheduler(JavaPlugin plugin, long period, long endTicks, long lightUpPeriod, Location location) {
        super(plugin, period, endTicks);
        this.lightUpPeriod = lightUpPeriod;
        this.block = location.getBlock();
    }

    @Override
    public void end() {
        super.end();
        BLOCK_FACES.forEach(blockFace -> {
            Block blockLightUp = block.getRelative(blockFace);
            if (blockLightUp.getType().equals(Material.LIGHT)) {
                WorldUtil.changeBlock(blockLightUp.getLocation(), Material.AIR);
            }
        });
    }

    @Override
    public void run() {
        if (count % lightUpPeriod == 0) {
            lightLevel ++;
            lightLevel = Math.min(lightLevel, 15);
            BLOCK_FACES.forEach(blockFace -> {
                Block blockLightUp = block.getRelative(blockFace);
                if (blockLightUp.getType().equals(Material.AIR) || blockLightUp.getType().equals(Material.LIGHT)) {
                    WorldUtil.placeLightSource(blockLightUp, lightLevel);
                }
            });
        }
        super.run();
    }

}
