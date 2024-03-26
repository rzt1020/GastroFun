package cn.myrealm.gastrofun.utils;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;

import java.util.Objects;

/**
 * @author rzt1020
 */
public class WorldUtil {
    private WorldUtil(){

    }

    public static void spawnParticle(Particle particle, Location location, int amount, float offset) {
        World world = location.getWorld();
        if (Objects.isNull(world)) {
            return;
        }
        world.spawnParticle(particle, location, amount, offset, offset, offset);
    }

    public static void playSound(Sound sound, Location location, float volume, float pitch) {
        World world = location.getWorld();
        if (Objects.isNull(world)) {
            return;
        }
        world.playSound(location, sound, volume, pitch);
    }
    public static long getWorldTime(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (Objects.isNull(world)) {
            return -1;
        }
        return world.getFullTime();
    }

    public static void changeBlock(Location location, Material type) {
        location.getBlock().setType(type);
    }

    public static void placeLightSource(Block block, int lightLevel) {
        if (!block.getType().equals(Material.LIGHT)) {
            changeBlock(block.getLocation(), Material.LIGHT);
        }
        Levelled level = (Levelled) block.getBlockData();
        level.setLevel(lightLevel);
        block.setBlockData(level, true);
    }
}
