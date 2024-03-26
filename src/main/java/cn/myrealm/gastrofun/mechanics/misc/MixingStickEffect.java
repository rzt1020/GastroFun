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
        PacketUtil.spawnItemDisplay(playerList, location.clone().add(-0.1,0.2,-0.1), DefaultItems.MIXING_SPOON.generateItemStack(), entityId,null, null);
        PacketUtil.rotateEntity(playerList, entityId, 0, 15);
    }

    public void animate() {
        new BukkitRunnable() {
            double angle = 0; // 开始的角度

            @Override
            public void run() {
                // 计算新的X和Z位置（Y位置不变）
                double x = 0.025 * Math.cos(angle);
                double z = 0.025 * Math.sin(angle);

                // 创建一个向量，表示从当前位置到新位置的位移
                Vector displacement = new Vector(x, 0, z);

                // 使用之前定义的函数发送数据包，更新实体位置
                PacketUtil.moveEntityWithPacket(players, entityId, displacement);

                // 更新角度，准备下一次移动
                angle += Math.PI / 20; // 每次移动增加的角度

                // 如果角度超过2π（即一圈），从0开始
                if (angle >= 2 * Math.PI) {
                    angle = 0;
                }
            }
        }.runTaskTimer(GastroFun.plugin, 0, 1);
    }
}
