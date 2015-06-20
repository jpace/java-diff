package org.incava.diff;

/**
 * Represents a difference, as used in <code>Diff</code>. A difference consists
 * of two pairs of starting and ending points, each pair representing either the
 * "from" or the "to" collection passed to <code>Diff</code>. If an ending point
 * is -1, then the difference was either a deletion or an addition. For example,
 * if <code>getDeletedEnd()</code> returns -1, then the difference represents an
 * addition.
 */
public class Difference {
    public static final Integer NONE = -1;
    
    /**
     * The point at which the deletion starts.
     */
    private final Integer delStart;

    /**
     * The point at which the deletion ends.
     */
    private final Integer delEnd;

    /**
     * The point at which the addition starts.
     */
    private final Integer addStart;

    /**
     * The point at which the addition ends.
     */
    private final Integer addEnd;

    /**
     * Creates the difference for the given start and end points for the
     * deletion and addition.
     */
    public Difference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
        this.delStart = delStart;
        this.delEnd   = delEnd;
        this.addStart = addStart;
        this.addEnd   = addEnd;
    }

    /**
     * The point at which the deletion starts, if any. A value equal to
     * <code>NONE</code> means this is an addition.
     */
    public Integer getDeletedStart() {
        return delStart;
    }

    /**
     * The point at which the deletion ends, if any. A value equal to
     * <code>NONE</code> means this is an addition.
     */
    public Integer getDeletedEnd() {
        return delEnd;
    }

    /**
     * The point at which the addition starts, if any. A value equal to
     * <code>NONE</code> means this must be a deletion.
     */
    public Integer getAddedStart() {
        return addStart;
    }

    /**
     * The point at which the addition ends, if any. A value equal to
     * <code>NONE</code> means this must be a deletion.
     */
    public Integer getAddedEnd() {
        return addEnd;
    }

    public boolean isAdd() {
        return delEnd == NONE;
    }

    public boolean isDelete() {
        return addEnd == NONE;
    }

    public boolean isChange() {
        return addEnd != NONE && delEnd != NONE;
    }

    /**
     * Compares this object to the other for equality. Both objects must be of
     * type Difference, with the same starting and ending points.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Difference) {
            Difference other = (Difference)obj;
            return (delStart == other.delStart && 
                    delEnd   == other.delEnd && 
                    addStart == other.addStart && 
                    addEnd   == other.addEnd);
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        int hash = 1;
        for (Integer i : new Integer[] { delStart, delEnd, addStart, addEnd }) {
            hash = hash * 17 + i.hashCode();
        }
        return hash;
    }

    /**
     * Returns a string representation of this difference.
     */
    public String toStringOrig() {
        StringBuilder sb = new StringBuilder();
        sb.append("del: [").append(delStart).append(", ").append(delEnd).append(']');
        sb.append(' ');
        sb.append("add: [").append(addStart).append(", ").append(addEnd).append(']');
        sb.append("add: [").append(addStart).append(", ").append(addEnd).append(']');
        return sb.toString();
    }

    protected String toString(int from, int to) {
        StringBuilder sb = new StringBuilder("[");
        if (from != NONE) {
            sb.append(from);
            if (to != NONE) {
                sb.append(", ").append(to);
            }
        }
        else {
            sb.append(to);
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * Returns a string representation of this difference.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString(delStart, delEnd));
        sb.append(" => ");
        sb.append(toString(addStart, addEnd));
        return sb.toString();
    }
}
