package me.rejomy.skypvp.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class RussianTimeUnitFormat {

    /**
     * @param firstLetter Sometimes in russian we need to choose like "через 1 секунду" or "осталась 1 секунда"
     */
    public String addEndingToSeconds(long seconds, String firstLetter) {
        long end = seconds % 10;

        return
                end == 1? firstLetter :
                end >= 2 && end <= 4? "ы" :
                "";
    }
}
