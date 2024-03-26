package cn.myrealm.gastrofun.mechanics.misc;


import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.enums.mechanics.DefaultItems;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rzt1020
 */
public class MixingStickEffect {
    private final int entityId;
    private final List<Player> players = new ArrayList<>();
    public MixingStickEffect(int entityId) {
        this.entityId = entityId;
    }

    public void display(List<Player> playerList, Location location) {
        players.addAll(playerList);
        PacketUtil.spawnItemDisplay(playerList, location.clone().add(-0.04,-0.2,0.25), DefaultItems.MIXING_SPOON.generateItemStack(), entityId,null, null);
        PacketUtil.rotateEntity(playerList, entityId, 0, 105);
    }

    public void animate() {
        new BukkitRunnable() {
            double angle = 0; // 定义角度，用于计算x和z轴上的位移
            double height = 0; // 定义高度，用于计算y轴上的位移

            @Override
            public void run() {
                double radius = 0.025; // 设定半径大小
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                double y = 0.015 * Math.sin(height); // 使用正弦函数来模拟上下运动

                Vector displacement = new Vector(x, y, z);

                PacketUtil.moveEntityWithPacket(players, entityId, displacement);

                angle += Math.PI / 20; // 每次迭代增加的角度，用于控制旋转速度
                height += Math.PI / 5;

                if (angle >= 2 * Math.PI) {
                    angle = 0; // 当完成一圈时，重置角度
                }
            }
        }.runTaskTimer(GastroFun.plugin, 0, 1);
    }
}
