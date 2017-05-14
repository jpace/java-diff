package org.incava.diff;

import java.util.Comparator;

/**
 * An object that does not implement Comparable; it is compared using a
 * Comparator.
 */
public class NoncomparableObject {
    public static class NoncomparableObjectComparator implements Comparator<NoncomparableObject> {
        public int compare(NoncomparableObject x, NoncomparableObject y) {
            if (x == null) {
                if (y == null) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
            else if (y == null) {
                return 1;
            }
            else {
                return x.str.compareTo(y.str);
            }
        }
                
        public boolean equals(NoncomparableObject str, NoncomparableObject y) {
            return compare(str, y) == 0;
        }
    }

    private String str;

    public NoncomparableObject(String str) {
        this.str = str;
    }

    public boolean equals(Object obj) {
        return doCompare(obj) == 0;
    }

    public int doCompare(Object obj) {
        if (obj instanceof NoncomparableObject) {
            NoncomparableObject other = (NoncomparableObject)obj;
            return str.compareTo(other.str);
        }
        else {
            return -1;
        }
    }

    public String toString() {
        return "\"" + str + "\"";
    }

    public int hashCode() {
        return str.hashCode();
    }
}
