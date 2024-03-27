package cn.myrealm.gastrofun.mechanics.misc;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.mechanics.items.DefaultItem;
import cn.myrealm.gastrofun.utils.ItemUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

/**
 * @author rzt1020
 */
public class MixingSpoon {
    private final List<Player> players;
    private final int entityId;
    private final Location location;
    private final Quaternionf rotation;
    public MixingSpoon(List<Player> players, int entityId, Location location, Quaternionf rotation) {
        this.players = players;
        this.entityId = entityId;
        this.location = location;
        this.rotation = rotation;
    }

    public void display() {
        PacketUtil.spawnItemDisplay(players, location.clone().add(-0.01,0, -0.15), DefaultItems.MIXING_SPOON.generateItemStack(), entityId,new Vector3f(0.75f), rotation);
    }

    public void animate() {
        new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                double radius = 0.025; // 设定半径大小
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                Vector displacement = new Vector(x, 0, z);

                PacketUtil.moveEntityWithPacket(players, entityId, displacement);

                angle += Math.PI / 20;

                if (angle >= 2 * Math.PI) {
                    angle = 0; // 当完成一圈时，重置角度
                }
            }
        }.runTaskTimer(GastroFun.plugin, 0, 1);
    }
}
