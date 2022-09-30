package br.ies.socket.utils;

import java.util.regex.Pattern;

public class Patterns {
    public static Pattern REPEATED_CHARACTERS_TWO_TIMES = Pattern.compile("(.)\\1{3,}");
    public static Pattern NON_DIGIT = Pattern.compile("\\D");
}
