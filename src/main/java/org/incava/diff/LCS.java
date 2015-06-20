package org.incava.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

public class LCS<ObjectType> {
    /**
     * The source array, AKA the "from" values.
     */
    private final List<ObjectType> from;

    /**
     * The target array, AKA the "to" values.
     */
    private final List<ObjectType> to;

    /**
     * The comparator used, if any.
     */
    private final Comparator<ObjectType> comparator;

    /**
     * Constructs an LCS for the two arrays, using the given comparator.
     */
    public LCS(ObjectType[] from, ObjectType[] to, Comparator<ObjectType> comp) {
        this(Arrays.asList(from), Arrays.asList(to), comp);
    }

    /**
     * Constructs an LCS for the two arrays, using the default comparison
     * mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public LCS(ObjectType[] from, ObjectType[] to) {
        this(from, to, null);
    }

    /**
     * Constructs an LCS for the two collections, using the default comparison
     * mechanism between the objects, such as <code>equals</code> and
     * <code>compareTo</code>.
     */
    public LCS(List<ObjectType> from, List<ObjectType> to) {
        this(from, to, null);
    }

    /**
     * Constructs an LCS for the two collections, using the given comparator.
     */
    public LCS(List<ObjectType> from, List<ObjectType> to, Comparator<ObjectType> comp) {
        this.from = from;
        this.to = to;
        this.comparator = comp;
    }

    /**
     * Returns an array of the longest common subsequences.
     */
    public List<Integer> getMatches() {
        int fromStart = 0;
        int fromEnd = from.size() - 1;

        int toStart = 0;
        int toEnd = to.size() - 1;

        TreeMap<Integer, Integer> matches = new TreeMap<Integer, Integer>();

        // common beginning and ending elements:
        while (fromStart <= fromEnd && toStart <= toEnd && equals(comparator, from.get(fromStart), to.get(toStart))) {
            matches.put(fromStart++, toStart++);
        }

        while (fromStart <= fromEnd && toStart <= toEnd && equals(comparator, from.get(fromEnd), to.get(toEnd))) {
            matches.put(fromEnd--, toEnd--);
        }

        addMatches(matches, fromStart, fromEnd, toStart, toEnd);
        
        return toList(matches);
    }

    public void addMatches(TreeMap<Integer, Integer> matches, int fromStart, int fromEnd, int toStart, int toEnd) {
        Map<ObjectType, List<Integer>> toMatches = getToMatches(toStart, toEnd);

        LCSTable links = new LCSTable();
        Thresholds thresh = new Thresholds();

        for (int idx = fromStart; idx <= fromEnd; ++idx) {
            ObjectType fromElement = from.get(idx); // keygen here.
            List<Integer> positions = toMatches.get(fromElement);

            if (positions == null) {
                continue;
            }
            
            Integer k = 0;
            ListIterator<Integer> pit = positions.listIterator(positions.size());
            while (pit.hasPrevious()) {
                Integer j = pit.previous();
                k = thresh.insert(j, k);
                if (k != null) {
                    links.update(idx, j, k);
                }   
            }
        }

        if (!thresh.isEmpty()) {
            Integer ti = thresh.lastKey();
            Map<Integer, Integer> chain = links.getChain(ti);
            matches.putAll(chain);
        }
    }

    public Map<ObjectType, List<Integer>> createMatchesMap() {
        if (comparator == null) {
            if (from.size() > 0 && from.get(0) instanceof Comparable) {
                // this uses the Comparable interface
                return new TreeMap<ObjectType, List<Integer>>();
            }
            else {
                // this just uses hashCode()
                return new HashMap<ObjectType, List<Integer>>();
            }
        }
        else {
            // we don't really want them sorted, but this is the only Map
            // implementation (as of JDK 1.4) that takes a comparator.
            return new TreeMap<ObjectType, List<Integer>>(comparator);
        }
    }

    public Map<ObjectType, List<Integer>> getToMatches(int toStart, int toEnd) {
        Map<ObjectType, List<Integer>> toMatches = createMatchesMap();

        for (int toIdx = toStart; toIdx <= toEnd; ++toIdx) {
            ObjectType key = to.get(toIdx);
            List<Integer> positions = toMatches.get(key);
            if (positions == null) {
                positions = new ArrayList<Integer>();
                toMatches.put(key, positions);
            }
            positions.add(toIdx);
        }

        return toMatches;
    }

    /**
     * Compares the two objects, using the comparator provided with the
     * constructor, if any.
     */
    protected boolean equals(Comparator<ObjectType> comparator, ObjectType x, ObjectType y) {
        return comparator == null ? x.equals(y) : comparator.compare(x, y) == 0;
    }

    /**
     * Converts the map into a list.
     */
    protected static List<Integer> toList(TreeMap<Integer, Integer> map) {
        int size = map.isEmpty() ? 0 : 1 + map.lastKey();
        ArrayList<Integer> list = new ArrayList<Integer>(Collections.nCopies(size, (Integer)null));
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            list.set(entry.getKey(), entry.getValue());
        }
        return list;
    }
}
