package cn.myrealm.gastrofun.mechanics.ingredients;


import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author rzt1020
 */
public class SkilletIngredientSecond extends BaseSkilletIngredient {
    public SkilletIngredientSecond(int entityId) {
        this.entityId = entityId + 2;
        scale = new Vector3f(.4f, .4f, .4f);
        rotation = new Quaternionf().rotateX((float)Math.PI / 2);
        offset = new Vector(0.14, -0.4, -0.17);
        start = 100;
        mid = 105;
        end = 110;
    }

}
