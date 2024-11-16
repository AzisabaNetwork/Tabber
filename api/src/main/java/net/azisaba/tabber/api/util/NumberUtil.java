package net.azisaba.tabber.api.util;

import org.jetbrains.annotations.NotNull;

public class NumberUtil {
    private static final int BASE = 65534;
    private static final char ESCAPE_CHAR = '\\';

    public static @NotNull String compressNumberReverse(double number) {
        return compressNumber(1.073741823E9 - number);
    }

    public static @NotNull String compressNumber(double number) {
        int wholePart = (int) number;

        StringBuilder sb = new StringBuilder();
        appendWholePart(sb, wholePart);

        while (sb.length() < 2) {
            sb.insert(0, '\u0000');
        }

        char decimalChar = getDecimalChar(number, wholePart);
        if (decimalChar != '\u0000') {
            sb.append(decimalChar);
        }
        return sb.toString();
    }

    public static @NotNull String compressNumberReverse(int number) {
        return compressNumber(1.073741823E9 - number);
    }

    public static @NotNull String compressNumber(int number) {
        StringBuilder sb = new StringBuilder();
        appendWholePart(sb, number);
        return sb.toString();
    }

    private static char getDecimalChar(double number, int wholePart) {
        char decimalChar = (char) ((int) ((number - wholePart) * BASE));
        if (decimalChar >= ESCAPE_CHAR) {
            decimalChar++;
        }
        return decimalChar;
    }

    private static void appendWholePart(StringBuilder sb, int wholePart) {
        while (wholePart > 0) {
            char digit = (char) (wholePart % BASE);
            if (digit >= ESCAPE_CHAR) {
                digit++;
            }
            sb.insert(0, digit);
            wholePart /= BASE;
        }
    }
}
