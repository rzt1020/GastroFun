package cn.myrealm.gastrofun.mechanics.persistent;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Quaternionf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author rzt1020
 */
public class QuaternionTagType implements PersistentDataType<byte[], Quaternionf> {

    private final Charset charset = StandardCharsets.UTF_8;

    @Override
    public @NonNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NonNull Class<Quaternionf> getComplexType() {
        return Quaternionf.class;
    }

    @Override
    public byte @NonNull[] toPrimitive(Quaternionf quaternion, @NonNull PersistentDataAdapterContext context) {
        String quaternionString = quaternion.x + "," +
                quaternion.y + "," +
                quaternion.z + "," +
                quaternion.w;
        return quaternionString.getBytes(charset);
    }

    @Override
    public @NonNull Quaternionf fromPrimitive(byte @NonNull[] bytes, @NonNull PersistentDataAdapterContext context) {
        String quaternionString = new String(bytes, charset);
        String[] parts = quaternionString.split(",");

        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid quaternion data.");
        }

        return new Quaternionf(
                Float.parseFloat(parts[0]),
                Float.parseFloat(parts[1]),
                Float.parseFloat(parts[2]),
                Float.parseFloat(parts[3])
        );
    }
}
