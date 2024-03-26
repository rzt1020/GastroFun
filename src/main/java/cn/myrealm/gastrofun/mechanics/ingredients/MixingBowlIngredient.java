package cn.myrealm.gastrofun.mechanics.ingredients;


import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author rzt1020
 */
public class MixingBowlIngredient extends BaseIngredient{
    public MixingBowlIngredient (int entityId) {
        this.entityId = entityId + 1;
        scale = new Vector3f(.4f, .4f, .4f);
        rotation = new Quaternionf().rotateX((float)Math.PI / 2);
        offset = new Vector(0, -0.3, 0);
    }
}
