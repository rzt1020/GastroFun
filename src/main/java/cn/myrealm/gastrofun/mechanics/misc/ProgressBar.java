package cn.myrealm.gastrofun.mechanics.misc;

import cn.myrealm.gastrofun.enums.mechanics.Fonts;
import cn.myrealm.gastrofun.utils.BasicUtil;
import cn.myrealm.gastrofun.utils.PacketUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rzt1020
 */
public class ProgressBar {
    private final int entityId;
    private String barString;
    private int progress;
    private final List<Player> playerList = new ArrayList<>();
    public ProgressBar(int entityId) {
        this.entityId = entityId;
        barString = Fonts.PROGRESS_BAR_0.toString();
        progress = 0;
    }

    public void display(List<Player> playerList, Location location) {
        PacketUtil.spawnTextDisplay(playerList, location,barString, entityId, null, null);
    }

    public void update(List<Player> playerList, Location location) {
        location = location.clone().add(0.5,0,0.5);
        this.playerList.addAll(playerList);
        for (Player player : playerList) {
            PacketUtil.updateTextDisplay(List.of(player), barString, entityId, null, BasicUtil.directionToQuaternionDirectlyY(location, player.getLocation()));
        }
    }
    public void addProgress() {
        switch (progress) {
            case 0 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_10.toString();
            }
            case 10 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_20.toString();
            }
            case 20 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_30.toString();
            }
            case 30 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_40.toString();
            }
            case 40 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_50.toString();
            }
            case 50 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_60.toString();
            }
            case 60 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_70.toString();
            }
            case 70 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_80.toString();
            }
            case 80 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_90.toString();
            }
            case 90 -> {
                progress += 10;
                barString = Fonts.PROGRESS_BAR_100.toString();
            }
            default -> {
                progress = 0;
                barString = Fonts.PROGRESS_BAR_0.toString();
            }
        }
    }

    public void reset() {
        barString = Fonts.PROGRESS_BAR_0.toString();
        progress = 0;
        PacketUtil.removeEntity(playerList, entityId);
        playerList.clear();
    }
}
