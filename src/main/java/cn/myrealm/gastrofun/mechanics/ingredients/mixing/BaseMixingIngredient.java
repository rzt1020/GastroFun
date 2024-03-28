package cn.myrealm.gastrofun.mechanics.ingredients.mixing;


import cn.myrealm.gastrofun.mechanics.ingredients.BaseIngredient;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * @author rzt1020
 */
public abstract class BaseMixingIngredient extends BaseIngredient {
    public BaseMixingIngredient(int entityId) {
        this.entityId = entityId+1;
        scale = new Vector3f(.4f, .4f, .4f);
        rotation = new Quaternionf().rotateX((float)Math.PI / 2);
    }
}
