package cn.myrealm.gastrofun.utils;


import cn.myrealm.gastrofun.GastroFun;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;


/**
 * @author rzt1020
 */
public class PacketUtil {
    private PacketUtil() {
    }
    public static void swingItem(Player player) {
        PacketContainer animationPacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.ANIMATION);

        animationPacket.getIntegers().write(0, player.getEntityId());
        animationPacket.getIntegers().write(1, 0);

        GastroFun.protocolManager.sendServerPacket(player, animationPacket);
    }
    public static void changeBlock(List<Player> player, Location blockLocation, Material material) {
        PacketContainer blockChangePacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);

        blockChangePacket.getBlockPositionModifier().write(0, new BlockPosition(blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ()));
        blockChangePacket.getBlockData().write(0, WrappedBlockData.createData(material));

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, blockChangePacket);
        }
    }
    public static void spawnItemDisplay(List<Player> player, Location location, ItemStack displayItem, int entityId, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        UUID entityUuid = UUID.randomUUID();

        spawnPacket.getIntegers()
                .write(0, entityId);
        spawnPacket.getDoubles().write(0, location.getX() + 0.5d)
                .write(1, location.getY() + 0.5d)
                .write(2, location.getZ() + 0.5d);

        spawnPacket.getUUIDs().write(0, entityUuid);
        spawnPacket.getEntityTypeModifier().write(0, EntityType.ITEM_DISPLAY);

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, spawnPacket);
        }
        updateItemDisplay(player, displayItem, entityId, scale, rotation);
    }
    public static void updateItemDisplay(List<Player> player, ItemStack displayItem, int entityId, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher entityMetaData = new WrappedDataWatcher();
        if (Bukkit.getVersion().contains("1.20.1") || (Bukkit.getVersion().contains("1.20")
                && (Bukkit.getVersion().split("1.20").length == 1 ||
                !Bukkit.getVersion().split("1.20")[1].startsWith(".")))) {
            if (Objects.nonNull(scale)) {
                entityMetaData.setObject(11, WrappedDataWatcher.Registry.get(Vector3f.class), scale);
            }
            if (Objects.nonNull(rotation)) {
                entityMetaData.setObject(12, WrappedDataWatcher.Registry.get(Quaternionf.class), rotation);
            }
            entityMetaData.setObject(22, WrappedDataWatcher.Registry.getItemStackSerializer(false), displayItem);

        } else {
            if (Objects.nonNull(scale)) {
                entityMetaData.setObject(12, WrappedDataWatcher.Registry.get(Vector3f.class), scale);
            }
            if (Objects.nonNull(rotation)) {
                entityMetaData.setObject(13, WrappedDataWatcher.Registry.get(Quaternionf.class), rotation);
            }
            entityMetaData.setObject(23, WrappedDataWatcher.Registry.getItemStackSerializer(false), displayItem);
        }

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        for (WrappedWatchableObject entry : entityMetaData.getWatchableObjects()) {
            if (entry != null) {
                WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
                wrappedDataValueList.add(new WrappedDataValue(watcherObject.getIndex(), watcherObject.getSerializer(), entry.getRawValue()));
            }
        }
        metaDataPacket.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, metaDataPacket);
        }
    }


    public static void spawnTextDisplay(List<Player> player, Location location, String text, int entityId, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        UUID entityUuid = UUID.randomUUID();

        spawnPacket.getIntegers()
                .write(0, entityId);
        spawnPacket.getDoubles().write(0, location.getX() + 0.5d)
                .write(1, location.getY() + 0.5d)
                .write(2, location.getZ() + 0.5d);

        spawnPacket.getUUIDs().write(0, entityUuid);
        spawnPacket.getEntityTypeModifier().write(0, EntityType.TEXT_DISPLAY);

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, spawnPacket);
        }
        updateTextDisplay(player, text, entityId, scale, rotation);
    }

    public static void updateTextDisplay(List<Player> player, String text, int entityId, @Nullable Vector3f scale, @Nullable Quaternionf rotation) {
        PacketContainer metaDataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaDataPacket.getIntegers().write(0, entityId);

        WrappedDataWatcher entityMetaData = new WrappedDataWatcher();
        if (Bukkit.getVersion().contains("1.20.1") || (Bukkit.getVersion().contains("1.20")
                && (Bukkit.getVersion().split("1.20").length == 1 ||
                !Bukkit.getVersion().split("1.20")[1].startsWith(".")))) {
            if (Objects.nonNull(scale)) {
                entityMetaData.setObject(11, WrappedDataWatcher.Registry.get(Vector3f.class), scale);
            }
            if (Objects.nonNull(rotation)) {
                entityMetaData.setObject(12, WrappedDataWatcher.Registry.get(Quaternionf.class), rotation);
            }
            entityMetaData.setObject(22, WrappedDataWatcher.Registry.getChatComponentSerializer(false), WrappedChatComponent.fromText(text));
        } else {
            if (Objects.nonNull(scale)) {
                entityMetaData.setObject(12, WrappedDataWatcher.Registry.get(Vector3f.class), scale);
            }
            if (Objects.nonNull(rotation)) {
                entityMetaData.setObject(13, WrappedDataWatcher.Registry.get(Quaternionf.class), rotation);
            }
            entityMetaData.setObject(23, WrappedDataWatcher.Registry.getChatComponentSerializer(false), WrappedChatComponent.fromText(text));
        }

        byte initialMeta = 0x04;
        entityMetaData.setObject(27, WrappedDataWatcher.Registry.get(Byte.class), initialMeta);

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        for (WrappedWatchableObject entry : entityMetaData.getWatchableObjects()) {
            if (entry != null) {
                WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
                wrappedDataValueList.add(new WrappedDataValue(watcherObject.getIndex(), watcherObject.getSerializer(), entry.getRawValue()));
            }
        }
        metaDataPacket.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, metaDataPacket);
        }


    }
    public static void removeEntity(List<Player> player, int entityId) {
        PacketContainer entityDestroyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);

        entityDestroyPacket.getIntLists().write(0, Collections.singletonList(entityId));

        for (Player p : player) {
            GastroFun.protocolManager.sendServerPacket(p, entityDestroyPacket);
        }
    }

    public static void teleportEntity(List<Player> players, int entityId, Location location) {
        PacketContainer teleportPacket = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);


        teleportPacket.getIntegers().write(0, entityId);
        teleportPacket.getDoubles().write(0, location.getX() + 0.5);
        teleportPacket.getDoubles().write(1, location.getY() + 0.5);
        teleportPacket.getDoubles().write(2, location.getZ() + 0.5);


        for (Player player : players) {
            GastroFun.protocolManager.sendServerPacket(player, teleportPacket);
        }
    }

    public static void moveEntityWithPacket(List<Player> players, int entityId, Vector displacement) {

        PacketContainer entityMovePacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.REL_ENTITY_MOVE);

        entityMovePacket.getIntegers()
                .write(0, entityId);

        short deltaX = (short) (displacement.getX() * 4096);
        short deltaY = (short) (displacement.getY() * 4096);
        short deltaZ = (short) (displacement.getZ() * 4096);

        entityMovePacket.getShorts()
                .write(0, deltaX)
                .write(1, deltaY)
                .write(2, deltaZ);

        entityMovePacket.getBooleans()
                .write(0, true);

        for (Player player : players) {
            GastroFun.protocolManager.sendServerPacket(player, entityMovePacket);
        }
    }

    public static void rotateEntity(List<Player> players, int entityId, float yaw, float pitch) {
        PacketContainer entityHeadRotationPacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        entityHeadRotationPacket.getIntegers().write(0, entityId);
        entityHeadRotationPacket.getBytes().write(0, (byte) (yaw * 256 / 360));

        PacketContainer entityLookPacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);
        entityLookPacket.getIntegers().write(0, entityId);
        entityLookPacket.getBytes()
                .write(0, (byte) (yaw * 256 / 360))
                .write(1, (byte) (pitch * 256 / 360));
        entityLookPacket.getBooleans().write(0, true);

        for (Player player : players) {
            GastroFun.protocolManager.sendServerPacket(player, entityHeadRotationPacket);
            GastroFun.protocolManager.sendServerPacket(player, entityLookPacket);
        }
    }

    public static void tiltEntity(List<Player> players, int entityId, float roll, float pitch) {
        PacketContainer entityLookPacket = GastroFun.protocolManager.createPacket(PacketType.Play.Server.ENTITY_LOOK);

        entityLookPacket.getIntegers().write(0, entityId);
        entityLookPacket.getBytes()
                .write(0, (byte) (roll * 256 / 360))
                .write(1, (byte) (pitch * 256 / 360));

        entityLookPacket.getBooleans().write(0, true);

        for (Player player : players) {
            GastroFun.protocolManager.sendServerPacket(player, entityLookPacket);
        }
    }

}
