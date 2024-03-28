package cn.myrealm.gastrofun.mechanics.ingredients.mixing;


import org.bukkit.util.Vector;

/**
 * @author rzt1020
 */
public class MixingIngredientSecond extends BaseMixingIngredient {
    public MixingIngredientSecond(int entityId) {
        super(entityId + 2);
        offset = new Vector(-0.05, -0.35, -0.05);
    }
}
