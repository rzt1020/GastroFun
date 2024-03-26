package cn.myrealm.gastrofun.enums.mechanics;


/**
 * @author rzt1020
 */
public enum Fonts {
    // offsets
    OFFSET_1('\uEEE0'),
    OFFSET_2('\uEEE1'),
    // icons
    HUNGER('\uEEE2'),
    HUNGER_HALF('\uEEE3'),
    // progress bars
    PROGRESS_BAR_0('\uEEE4'),
    PROGRESS_BAR_10('\uEEE5'),
    PROGRESS_BAR_20('\uEEE6'),
    PROGRESS_BAR_30('\uEEE7'),
    PROGRESS_BAR_40('\uEEE8'),
    PROGRESS_BAR_50('\uEEE9'),
    PROGRESS_BAR_60('\uEEEA'),
    PROGRESS_BAR_70('\uEEEB'),
    PROGRESS_BAR_80('\uEEEC'),
    PROGRESS_BAR_90('\uEEED'),
    PROGRESS_BAR_100('\uEEEE');

    private final char font;
    Fonts(char font) {
        this.font = font;
    }

    public String getString() {
        return "§f" + font;
    }

    @Override
    public String toString() {
        return String.valueOf(font);
    }
}
