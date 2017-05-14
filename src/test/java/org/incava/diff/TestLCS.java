package org.incava.diff;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;

import static org.incava.diff.Util.list;

public class TestLCS extends TestCase {
    public TestLCS(String name) {
        super(name);
    }

    public <T> LCS<T> assertLCS(LCS<T> lcs, Integer[] expected) {
        List<Integer> lcses = lcs.getMatches();
        int count = Math.max(expected.length, lcses.size());
        for (int idx = 0; idx < count; ++idx) {
            assertEquals("expected[" + idx +"]", expected[idx], lcses.get(idx));
        }
        return lcs;
    }

    protected <T> LCS<T> assertLCS(List<T> a, List<T> b, Integer[] expected) {
        LCS<T> lcs = new LCS<T>(a, b);
        return assertLCS(lcs, expected);
    }

    protected <T> LCS<T> assertLCS(List<T> a, List<T> b, Comparator<T> comparator, Integer[] expected) {
        LCS<T> lcs = new LCS<T>(a, b, comparator);
        return assertLCS(lcs, expected);
    }

    public Integer[] array(Integer ... nums) {
        return nums;
    }

    public void testStringSame() {
        assertLCS(list("a"), 
                  list("a"),
                  array(0));
    }

    public void testStringFromNone() {
        assertLCS(new ArrayList<String>(), 
                  list("a"),
                  array());
    }

    public void testStringToNone() {
        assertLCS(list("a"),
                  new ArrayList<String>(), 
                  array());
    }
    
    public void testStringSameSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(list("a", "b"), 
                  list("a", "b"), 
                  array(0, 1));
    }
    
    public void testStringDiffDiffSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(list("a", "b", "c"),
                  list("a", "b", "d"),
                  array(0, 1));
    }

    public void testStringDiffSameSame() {
        Integer[] expected = new Integer[] { null, 1, 2 };
        assertLCS(list("a", "b", "c"),
                  list("x", "b", "c"),
                  array(null, 1, 2));
    }

    public void testStringDiffSameDiffSameSame() {
        assertLCS(list("a", "b", "c", "d", "e"),
                  list("x", "b", "x", "d", "e"), 
                  array(null, 1, null, 3, 4));
    }

    public void testStringDiffDiffSameSameSame() {
        assertLCS(list("a", "b", "c", "d", "e"),
                  list("x", "y", "c", "d", "e"), 
                  array(null, null, 2, 3, 4));
    }

    public void testStringDiffDiffSameSameSameDiff() {
        assertLCS(list("a", "b", "c", "d", "e", "f"),
                  list("x", "y", "c", "d", "e", "z"), 
                  array(null, null, 2, 3, 4));
    }

    public List<NoncomparableObject> noncomps(String ... strs) {
        List<NoncomparableObject> list = new ArrayList<NoncomparableObject>();
        for (String str : strs) {
            list.add(new NoncomparableObject(str));
        }
        return list;
    }

    public void testSame() {
        assertLCS(noncomps("a"), 
                  noncomps("a"),
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(0));
    }

    public void testFromNone() {
        assertLCS(new ArrayList<NoncomparableObject>(), 
                  noncomps("a"),
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array());
    }

    public void testToNone() {
        assertLCS(noncomps("a"),
                  new ArrayList<NoncomparableObject>(), 
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array());
    }
    
    public void testSameSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(noncomps("a", "b"), 
                  noncomps("a", "b"), 
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(0, 1));
    }
    
    public void testDiffDiffSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(noncomps("a", "b", "c"),
                  noncomps("a", "b", "d"),
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(0, 1));
    }

    public void testDiffSameSame() {
        Integer[] expected = new Integer[] { null, 1, 2 };
        assertLCS(noncomps("a", "b", "c"),
                  noncomps("x", "b", "c"),
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(null, 1, 2));
    }

    public void testDiffSameDiffSameSame() {
        assertLCS(noncomps("a", "b", "c", "d", "e"),
                  noncomps("x", "b", "x", "d", "e"), 
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(null, 1, null, 3, 4));
    }

    public void testDiffDiffSameSameSame() {
        assertLCS(noncomps("a", "b", "c", "d", "e"),
                  noncomps("x", "y", "c", "d", "e"), 
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(null, null, 2, 3, 4));
    }

    public void testDiffDiffSameSameSameDiff() {
        assertLCS(noncomps("a", "b", "c", "d", "e", "f"),
                  noncomps("x", "y", "c", "d", "e", "z"), 
                  new NoncomparableObject.NoncomparableObjectComparator(),
                  array(null, null, 2, 3, 4));
    }

    public List<ComparableObject> comps(String ... strs) {
        List<ComparableObject> list = new ArrayList<ComparableObject>();
        for (String str : strs) {
            list.add(new ComparableObject(str));
        }
        return list;
    }

    public void testComparableSame() {
        assertLCS(comps("a"), 
                  comps("a"),
                  array(0));
    }

    public void testComparableFromNone() {
        assertLCS(new ArrayList<ComparableObject>(), 
                  comps("a"),
                  array());
    }

    public void testComparableToNone() {
        assertLCS(comps("a"),
                  new ArrayList<ComparableObject>(), 
                  array());
    }
    
    public void testComparableSameSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(comps("a", "b"), 
                  comps("a", "b"), 
                  array(0, 1));
    }
    
    public void testComparableDiffDiffSame() {
        Integer[] expected = new Integer[] { 0, 1 };
        assertLCS(comps("a", "b", "c"),
                  comps("a", "b", "d"),
                  array(0, 1));
    }

    public void testComparableDiffSameSame() {
        Integer[] expected = new Integer[] { null, 1, 2 };
        assertLCS(comps("a", "b", "c"),
                  comps("x", "b", "c"),
                  array(null, 1, 2));
    }

    public void testComparableDiffSameDiffSameSame() {
        assertLCS(comps("a", "b", "c", "d", "e"),
                  comps("x", "b", "x", "d", "e"), 
                  array(null, 1, null, 3, 4));
    }

    public void testComparableDiffDiffSameSameSame() {
        assertLCS(comps("a", "b", "c", "d", "e"),
                  comps("x", "y", "c", "d", "e"), 
                  array(null, null, 2, 3, 4));
    }

    public void testComparableDiffDiffSameSameSameDiff() {
        assertLCS(comps("a", "b", "c", "d", "e", "f"),
                  comps("x", "y", "c", "d", "e", "z"), 
                  array(null, null, 2, 3, 4));
    }
}
