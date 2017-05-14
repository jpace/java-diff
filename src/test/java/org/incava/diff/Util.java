package org.incava.diff;

import java.util.ArrayList;
import java.util.List;

public class Util {
    @SafeVarargs
    public static <T> List<T> list(T ... elements) {
        List<T> ary = new ArrayList<T>();
        for (T element : elements) {
            ary.add(element);
        }
        return ary;
    }
}
