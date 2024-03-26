package cn.myrealm.gastrofun.mechanics.ingredients;


import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author rzt1020
 */
public class SkilletIngredientThird extends BaseSkilletIngredient {
    public SkilletIngredientThird(int entityId) {
        this.entityId = entityId + 3;
        scale = new Vector3f(.4f, .4f, .4f);
        rotation = new Quaternionf().rotateX((float)Math.PI / 2);
        offset = new Vector(0.14, -0.4, 0.17);
        start = 200;
        mid = 205;
        end = 210;
    }


}
