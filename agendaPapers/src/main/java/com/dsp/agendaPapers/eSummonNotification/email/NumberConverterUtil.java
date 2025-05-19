package com.dsp.agendaPapers.eSummonNotification.email;

public class NumberConverterUtil {

    public static String toRoman(int number) {
        String[] romans = {
            "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
            "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"
        };
        return (number > 0 && number < romans.length) ? romans[number] : String.valueOf(number);
    }

    public static String toOrdinalWord(int number) {
        String[] ordinals = {
            "", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh",
            "Eighth", "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth",
            "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth"
        };
        return (number > 0 && number < ordinals.length) ? ordinals[number] : String.valueOf(number);
    }

    public static int parseIntSafe(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
