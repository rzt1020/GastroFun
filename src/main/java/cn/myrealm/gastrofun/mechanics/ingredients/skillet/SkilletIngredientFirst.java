package cn.myrealm.gastrofun.mechanics.ingredients.skillet;


import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author rzt1020
 */
public class SkilletIngredientFirst extends BaseSkilletIngredient {
    public SkilletIngredientFirst(int entityId) {
        this.entityId = entityId + 1;
        scale = new Vector3f(.4f, .4f, .4f);
        rotation = new Quaternionf().rotateX((float)Math.PI / 2);
        offset = new Vector(-0.16, -0.4, 0.05);
        start = 0;
        mid = 5;
        end = 10;
    }
}
