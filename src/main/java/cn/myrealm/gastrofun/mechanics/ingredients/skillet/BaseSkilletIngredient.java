package cn.myrealm.gastrofun.mechanics.ingredients.skillet;

import cn.myrealm.gastrofun.GastroFun;
import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import cn.myrealm.gastrofun.utils.PacketUtil;
import cn.myrealm.gastrofun.utils.WorldUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author rzt1020
 */
public abstract class BaseSkilletIngredient extends BaseIngredient {
    protected int currentDisplacement = 0;
    protected boolean random = GastroFun.RANDOM.nextBoolean();
    protected int pitch = random ? 0 : 360;
    protected int start, mid, end;

    @Override
    public void move(List<Player> players, Vector displacement, Location location) {
        updateRandomness();
        displacement = displacement.clone();
        long worldTime = WorldUtil.getWorldTime("world") % 300;
        if (start <= worldTime && worldTime < mid) {
            displacement.add(new Vector(0, 0.05, 0));
            currentDisplacement ++;
            sendRotatePacket(players);
        } else if (mid <= worldTime && worldTime < end && currentDisplacement > 0) {
            displacement.add(new Vector(0, -0.05, 0));
            currentDisplacement --;
            sendRotatePacket(players);
        }
        super.move(players, displacement, location);
    }

    private void sendRotatePacket(List<Player> players) {
        pitch += random ? 18 : -18;
        PacketUtil.rotateEntity(players, entityId, 0, pitch);
    }

    private void updateRandomness() {
        if (pitch == 0) {
            random = true;
        }
        if (pitch == 180) {
            random = GastroFun.RANDOM.nextBoolean();
        }
        if (pitch == 360) {
            random = false;
        }
    }
}
