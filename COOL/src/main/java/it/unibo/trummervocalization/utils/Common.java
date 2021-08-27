package it.unibo.trummervocalization.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class Common {

    public static <A, B> Map<A, B> removeMap(Map<A, B> map, A a) {
        Map<A, B> removedMap = new HashMap<>(map);
        removedMap.remove(a);
        return removedMap;
    }

    public static <A, B, C> Map<A, Map<B, C>> removeMap(Map<A, Map<B, C>> map, A a, B b) {
        Map<A, Map<B, C>> removedMap = new HashMap<>(map);
        removedMap.put(a, new HashMap<>(removedMap.get(a)));
        removedMap.get(a).remove(b);
        return removedMap;
    }

    public static String replaceLast(String input, String element, String substitute) {
        int i = input.lastIndexOf(element);
        return i >= 0 ? input.substring(0, i) + substitute + input.substring(i + element.length()) : input;
    }

    public static double roundDouble(double value, int nSignificantDigits) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.round(new MathContext(nSignificantDigits));
        return bd.doubleValue();
    }

    public static String stringifyDouble(double value) {
        return value == Math.floor(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    public static String stringifyDouble(double value, int nSignificantDigits) {
        return stringifyDouble(roundDouble(value, nSignificantDigits));
    }

    public static <A, B> Map<A, B> updateMap(Map<A, B> map, A a, B b) {
        Map<A, B> updatedMap = new HashMap<>(map);
        updatedMap.put(a, b);
        return updatedMap;
    }

}
