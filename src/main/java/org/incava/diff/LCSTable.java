package org.incava.diff;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * The links as used in the Diff/LCS code.
 */
public class LCSTable {
    private final Map<Integer, Object[]> links;
    
    public LCSTable() {
        links = new HashMap<Integer, Object[]>();
    }

    /**
     * Updates the value for the key <code>k</code>.
     */
    public void update(Integer i, Integer j, Integer k) {
        Object value = k > 0 ? links.get(k - 1) : null;
        links.put(k, new Object[] { value, i, j });
    }

    /**
     * Returns the links starting from <code>key</code>.
     */
    public Map<Integer, Integer> getChain(Integer key) {
        Map<Integer, Integer> chain = new HashMap<Integer, Integer>();
        Object[] link = links.get(key);
        while (link != null) {
            Integer x = (Integer)link[1];
            Integer y = (Integer)link[2];
            chain.put(x, y);
            link = (Object[])link[0];
        }
        return chain;
    }
}
