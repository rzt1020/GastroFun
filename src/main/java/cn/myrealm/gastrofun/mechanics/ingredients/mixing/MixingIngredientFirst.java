package cn.myrealm.gastrofun.mechanics.ingredients.mixing;


import org.bukkit.util.Vector;

/**
 * @author rzt1020
 */
public class MixingIngredientFirst extends BaseMixingIngredient {
    public MixingIngredientFirst(int entityId) {
        super(entityId + 1);
        offset = new Vector(0.05, -0.4, 0.05);
    }
}
