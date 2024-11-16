package net.azisaba.tabber.api.util;

import org.jetbrains.annotations.NotNull;

public class StringUtil {
    public static @NotNull String convertZtoA(@NotNull String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z') {
                chars[i] = (char) ('Z' - (c - 'A'));
            } else if (c >= 'a' && c <= 'z') {
                chars[i] = (char) ('z' - (c - 'a'));
            }
        }
        return new String(chars);
    }

    public static @NotNull String preValidateTeamName(@NotNull String teamName) {
        if (teamName.length() > 15) {
            return teamName.substring(0, 15);
        }
        return teamName;
    }
}
