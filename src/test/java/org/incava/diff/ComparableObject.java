package org.incava.diff;

public class ComparableObject implements Comparable<ComparableObject> {
    private String str;

    public ComparableObject(String str) {
        this.str = str;
    }

    public boolean equals(ComparableObject obj) {
        return compareTo(obj) == 0;
    }

    public int compareTo(ComparableObject obj) {
        return str.compareTo(obj.str);
    }

    public String toString() {
        return "\"" + str + "\"";
    }
}
