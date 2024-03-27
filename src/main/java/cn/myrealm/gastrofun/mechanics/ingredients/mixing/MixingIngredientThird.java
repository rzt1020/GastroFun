package cn.myrealm.gastrofun.mechanics.ingredients.mixing;


import org.bukkit.util.Vector;

/**
 * @author rzt1020
 */
public class MixingIngredientThird extends BaseMixingIngredient {
    public MixingIngredientThird(int entityId) {
        super(entityId + 3);
        offset = new Vector(0, -0.35, 0);
    }
}
