package cn.myrealm.gastrofun.mechanics.items;


/**
 * @author rzt1020
 */
public interface SchedulerAble {
    /**
     * call when scheduler completed
     */
    void schedulerCompleted();

    /**
     * get result
     * @return Object
     */
    Object getResult();
}
