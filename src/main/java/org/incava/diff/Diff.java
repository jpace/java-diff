package org.incava.diff;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Compares two collections, returning a list of the additions, changes, and
 * deletions between them. A <code>Comparator</code> may be passed as an
 * argument to the constructor, and will thus be used. If not provided, the
 * initial value in the <code>a</code> ("from") collection will be looked at to
 * see if it supports the <code>Comparable</code> interface. If so, its
 * <code>equals</code> and <code>compareTo</code> methods will be invoked on the
 * instances in the "from" and "to" collections; otherwise, for speed, hash
 * codes from the objects will be used instead for comparison.
 *
 * <p>The file FileDiff.java shows an example usage of this class, in an
 * application similar to the Unix "diff" program.</p>
 */
public class Diff <T extends Object> extends Differ<T, Difference> {
    /**
     * Constructs the Diff object for the two arrays, using the given comparator.
     */
    public Diff(T[] a, T[] b, Comparator<T> comp) {
        this(Arrays.asList(a), Arrays.asList(b), comp);
    }

    /**
     * Constructs the Diff object for the two arrays, using the default
     * comparison mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public Diff(T[] a, T[] b) {
        this(a, b, null);
    }

    /**
     * Constructs the Diff object for the two collections, using the default
     * comparison mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public Diff(List<T> a, List<T> b) {
        this(a, b, null);
    }

    /**
     * Constructs the Diff object for the two collections, using the given
     * comparator.
     */
    public Diff(List<T> a, List<T> b, Comparator<T> comp) {
        super(a, b, comp);
    }

    /**
     * Returns a legacy <code>Difference</code> See <code>Differ</code> to
     * return a subclass of Difference.
     */
    public Difference createDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
        return new Difference(delStart, delEnd, addStart, addEnd);
    }
}
