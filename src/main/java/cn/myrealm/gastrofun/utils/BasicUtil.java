package cn.myrealm.gastrofun.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author rzt1020
 */
public class BasicUtil {
    private BasicUtil() {
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void copyDirectory(File sourceDir, File targetDir) throws IOException {
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source (" + sourceDir.getPath() + ") must be a directory.");
        }

        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File[] items = sourceDir.listFiles();
        if (items != null) {
            for (File item : items) {
                File targetFile = new File(targetDir, item.getName());

                if (item.isDirectory()) {
                    copyDirectory(item, targetFile);
                } else {
                    copyFile(item, targetFile);
                }
            }
        }
    }

    private static void copyFile(File sourceFile, File targetFile) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;

        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);
            sourceChannel = fis.getChannel();
            targetChannel = fos.getChannel();

            targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) {
                try {
                    sourceChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (targetChannel != null) {
                try {
                    targetChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @author modified from <a href="https://blog.csdn.net/Yuan_xiaob/article/details/106033765">...</a>
     */
    public static List<String> stringArray(String content, int len) {
        int length = content.length();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < length; ) {
            int end = Math.min(i + len, length);
            String newContent = content.substring(i, end);

            if (end < length && content.charAt(end) != ' ') {
                int lastSpace = newContent.lastIndexOf(' ');
                if (lastSpace != -1) {
                    newContent = newContent.substring(0, lastSpace);
                    end = i + lastSpace + 1;
                }
            }
            newContent = newContent.trim();

            list.add(newContent);
            i = end;
        }

        return list;
    }

    public static List<String> parsePrefix(List<String> content, String prefix) {
        content.replaceAll(s -> prefix + s);
        return content;
    }

    public static int[] addElement(int[] array, int element) {
        if (array == null) {
            return new int[]{element};
        }

        int[] newArray = new int[array.length + 1];

        System.arraycopy(array, 0, newArray, 0, array.length);

        newArray[array.length] = element;

        return newArray;
    }

    public static int[] removeElement(int[] array, int element) {
        if (array == null || array.length == 0) {
            return array;
        }
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return array.clone();
        }
        int[] newArray = new int[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, index);
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        }
        return newArray;
    }

    public static List<Player> getNearbyPlayers(Location location, int radius) {
        assert location.getWorld() != null;
        Collection<Entity> entities = location.getWorld().getNearbyEntities(location, radius, radius, radius);
        List<Player> players = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                players.add((Player) entity);
            }
        }
        return players;
    }

    public static Player getNearestPlayer(Location location) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;
        Collection<Player> players = getNearbyPlayers(location, 16);

        for (Player player : players) {
            double distanceSquared = player.getLocation().distanceSquared(location);
            if (distanceSquared < nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestPlayer = player;
            }
        }

        return nearestPlayer;
    }

    public static Quaternionf directionToQuaternion(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        float angle;

        if (Math.abs(dx) > Math.abs(dz)) {
            if (dx > 0) {
                angle = (float) (Math.PI / 2);
            } else {
                angle = (float) (-Math.PI / 2);
            }
        } else {
            if (dz > 0) {
                angle = 0;
            } else {
                angle = (float) Math.PI;
            }
        }
        angle -= Math.PI / 2;
        return new Quaternionf().rotateY(angle);
    }

    public static Quaternionf directionToQuaternionDirectlyZ(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        float angle = (float)Math.atan2(dz, dx);
        angle += Math.PI / 2;

        return new Quaternionf().rotateZ(angle);
    }

    public static Quaternionf directionToQuaternionDirectlyY(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        float angle = (float)Math.atan2(dz, dx);
        angle -= Math.PI / 2;

        return new Quaternionf().rotateY(-angle);
    }

    public static String listToStringKey(List<String> list) {
        list.replaceAll(String::toUpperCase);
        Collections.sort(list);
        return String.join("|", list);
    }

}
