package org.incava.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Compares two collections, returning a list of the additions, changes, and
 * deletions between them. A <code>Comparator</code> may be passed as an
 * argument to the constructor, and will thus be used. If not provided, the
 * initial value in the <code>from</code> collection will be looked at to see if
 * it supports the <code>Comparable</code> interface. If so, its
 * <code>equals</code> and <code>compareTo</code> methods will be invoked on the
 * instances in the "from" and "to" collections; otherwise, for speed, hash
 * codes from the objects will be used instead for comparison.
 *
 * <p>The file FileDiff.java shows an example usage of this class, in an
 * application similar to the Unix "diff" program.</p>
 */
public abstract class Differ <ObjectType extends Object, DiffType extends Difference> {
    /**
     * The source array, AKA the "from" values.
     */
    private final List<ObjectType> from;

    /**
     * The target array, AKA the "to" values.
     */
    private final List<ObjectType> to;

    /**
     * The list of differences, as <code>DiffType</code> instances.
     */
    private final List<DiffType> diffs;

    /**
     * The point at which the pending deletion starts.
     */
    private Integer delStart = null;

    /**
     * The point at which the pending deletion ends.
     */
    private Integer delEnd = null;

    /**
     * The point at which the pending addition starts.
     */
    private Integer addStart = null;

    /**
     * The point at which the pending addition ends.
     */
    private Integer addEnd = null;

    /**
     * The comparator used, if any.
     */
    private final Comparator<ObjectType> comparator;

    /**
     * Constructs the Differ object for the two arrays, using the given comparator.
     */
    public Differ(ObjectType[] from, ObjectType[] to, Comparator<ObjectType> comp) {
        this(Arrays.asList(from), Arrays.asList(to), comp);
    }

    /**
     * Constructs the Differ object for the two arrays, using the default
     * comparison mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public Differ(ObjectType[] from, ObjectType[] to) {
        this(from, to, null);
    }

    /**
     * Constructs the Differ object for the two collections, using the default
     * comparison mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public Differ(List<ObjectType> from, List<ObjectType> to) {
        this(from, to, null);
    }

    /**
     * Constructs the Differ object for the two collections, using the given
     * comparator.
     */
    public Differ(List<ObjectType> from, List<ObjectType> to, Comparator<ObjectType> comp) {
        this.from = from;
        this.to = to;
        this.comparator = comp;
        this.diffs = new ArrayList<DiffType>();
    }

    /**
     * Runs diff and returns the results.
     */
    public List<DiffType> execute() {
        traverseSequences();
        addPending();
        return diffs;
    }

    /**
     * Runs diff and returns the results.
     *
     * @deprecated <code>execute</code> is a more accurate and descriptive name.
     */
    @Deprecated 
        public List<DiffType> diff() {
        return execute();
    }

    /**
     * Subclasses implement this to return their own subclass of
     * <code>Difference</code>.
     */
    public abstract DiffType createDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd);

    /**
     * Adds the last difference, if pending.
     */
    protected void addPending() {
        if (delStart != null) {
            diffs.add(createDifference(delStart, delEnd, addStart, addEnd));
            delStart = null;
            delEnd   = null;
            addStart = null;
            addEnd   = null;
        }
    }

    /**
     * Traverses the sequences, seeking the longest common subsequences,
     * invoking the methods <code>finishedFrom</code>, <code>finishedTo</code>,
     * <code>onFromNotTo</code>, and <code>onToNotFrom</code>.
     */
    protected void traverseSequences() {
        LCS<ObjectType> lcs = new LCS<ObjectType>(from, to, comparator);
        List<Integer> matches = lcs.getMatches();

        int toIdx = 0;
        int fromIdx = 0;
        int lastMatch = matches.size() - 1;
        
        while (fromIdx <= lastMatch) {
            Integer toElement = matches.get(fromIdx);

            if (toElement == null) {
                onFromNotTo(fromIdx, toIdx);
            }
            else {
                while (toIdx < toElement.intValue()) {
                    onToNotFrom(fromIdx, toIdx++);
                }

                onMatch(fromIdx, toIdx++);
            }
            fromIdx++;
        }

        traverseEndOfSequences(fromIdx, toIdx);
    }

    protected void traverseEndOfSequences(int fromIdx, int toIdx) {
        int lastFrom = from.size() - 1;
        int lastTo = to.size() - 1;

        while (fromIdx <= lastFrom || toIdx <= lastTo) {
            // last from?
            if (fromIdx == lastFrom + 1 && toIdx <= lastTo) {
                while (toIdx <= lastTo) {
                    onToNotFrom(fromIdx, toIdx++);
                }
            }

            // last to?
            if (toIdx == lastTo + 1 && fromIdx <= lastFrom) {
                while (fromIdx <= lastFrom) {
                    onFromNotTo(fromIdx++, toIdx);
                }
            }

            if (fromIdx <= lastFrom) {
                onFromNotTo(fromIdx++, toIdx);
            }

            if (toIdx <= lastTo) {
                onToNotFrom(fromIdx, toIdx++);
            }
        }
    }

    /**
     * Invoked for elements in <code>from</code> and not in <code>to</code>.
     */
    protected void onFromNotTo(int fromIdx, int toIdx) {
        if (delStart == null) {
            setIndices(fromIdx, fromIdx, toIdx, Difference.NONE);
        }
        else {
            delStart = Math.min(fromIdx, delStart);
            delEnd = Math.max(fromIdx, delEnd);
        }
    }

    /**
     * Invoked for elements in <code>to</code> and not in <code>from</code>.
     */
    protected void onToNotFrom(int fromIdx, int toIdx) {
        if (delStart == null) {
            setIndices(fromIdx, Difference.NONE, toIdx, toIdx);
        }
        else {
            addStart = Math.min(toIdx, addStart);
            addEnd = Math.max(toIdx, addEnd);
        }
    }

    private void setIndices(int delSt, int delEn, int addSt, int addEn) {
        delStart = delSt;
        delEnd = delEn;
        addStart = addSt;
        addEnd = addEn;
    }

    /**
     * Invoked for elements matching in <code>from</code> and <code>to</code>.
     */
    protected void onMatch(int fromIdx, int toIdx) {
        addPending();
    }
}
