package org.incava.diff;

import java.util.TreeMap;

/**
 * The map of thresholds as used in the Diff/LCS code.
 */
public class Thresholds extends TreeMap<Integer, Integer> {
    public static final long serialVersionUID = 1;

    /**
     * Returns whether the integer is not zero (including if it is not null).
     */
    protected static boolean isNonzero(Integer i) {
        return i != null && i.intValue() != 0;
    }

    /**
     * Returns whether the value in the map for the given index is greater than
     * the given value.
     */
    protected boolean isGreaterThan(Integer index, Integer val) {
        Integer lhs = get(index);
        return lhs != null && val != null && lhs.compareTo(val) > 0;
    }

    /**
     * Returns whether the value in the map for the given index is less than
     * the given value.
     */
    protected boolean isLessThan(Integer index, Integer val) {
        Integer lhs = get(index);
        return lhs != null && (val == null || lhs.compareTo(val) < 0);
    }

    /**
     * Returns the value for the greatest key in the map.
     */
    protected Integer getLastValue() {
        return get(lastKey());
    }

    /**
     * Adds the given value to the "end" of the threshold map, that is, with the
     * greatest index/key.
     */
    protected void append(Integer value) {
        Integer addIdx = null;
        if (isEmpty()) {
            addIdx = 0;
        }
        else {
            Integer lastKey = lastKey();
            addIdx = lastKey.intValue() + 1;
        }
        put(addIdx, value);
    }

    /**
     * Inserts the given values into the threshold map.
     */
    public Integer insert(Integer j, Integer k) {
        if (isNonzero(k) && isGreaterThan(k, j) && isLessThan(k.intValue() - 1, j)) {
            put(k, j);
            return k;
        }
        
        int hi = -1;
            
        if (isNonzero(k)) {
            hi = k.intValue();
        }
        else if (!isEmpty()) {
            hi = lastKey();
        }

        // off the end?
        if (hi == -1 || j.compareTo(getLastValue()) > 0) {
            append(j);
            return hi + 1;
        }
        
        // binary search for insertion point:
        int lo = 0;
        
        while (lo <= hi) {
            int     index = (hi + lo) / 2;
            Integer val   = get(index);
            int     cmp   = j.compareTo(val);

            if (cmp == 0) {
                return null;
            }
            else if (cmp > 0) {
                lo = index + 1;
            }
            else {
                hi = index - 1;
            }
        }
        
        put(lo, j);
        return lo;
    }
}
