package org.incava.diff;

import junit.framework.TestCase;

public class TestDifference extends TestCase {
    public TestDifference(String name) {
        super(name);
    }

    public void assertDifference(Integer expDelStart, Integer expDelEnd, Integer expAddStart, Integer expAddEnd, Difference d) {
        assertEquals(expDelStart, d.getDeletedStart());
        assertEquals(expDelEnd, d.getDeletedEnd());
        assertEquals(expAddStart, d.getAddedStart());
        assertEquals(expAddEnd, d.getAddedEnd());
    }
    
    public void testCtorAdded() {
        Difference d = new Difference(4, -1, 3, 17);
        assertDifference(4, -1, 3, 17, d);
    }
    
    public void testCtorDeleted() {
        Difference d = new Difference(1, 6, 9, -1);
        assertDifference(1, 6, 9, -1, d);
    }

    public void testCtorChanged() {
        Difference d = new Difference(4, 6, 11, 14);
        assertDifference(4, 6, 11, 14, d);
    }

    public void testEqualsTrue() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 6, 11, 14);
        assertEquals(db, da);
    }

    public void testEqualsFalseNull() {
        Difference d = new Difference(4, 6, 11, 14);
        assertFalse(d.equals(null));
    }

    public void testEqualsFalseNotDifference() {
        Difference d = new Difference(4, 6, 11, 14);
        assertFalse(d.equals("str"));
    }

    public void testEqualsFalseDeletedStart() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(2, 6, 11, 14);
        assertFalse(db.equals(da));
    }

    public void testEqualsFalseDeletedEnd() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 7, 11, 14);
        assertFalse(db.equals(da));
    }

    public void testEqualsFalseAddedStart() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 6, 10, 14);
        assertFalse(db.equals(da));
    }

    public void testEqualsFalseAddedEnd() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 6, 11, 18);
        assertFalse(db.equals(da));
    }

    public void assertHashCode(boolean expected, Difference da, Difference db) {
        int hca = da.hashCode();
        int hcb = db.hashCode();
        assertEquals("hca: " + hca + " == hcb: " + hcb, expected, hca == hcb);
    }

    public void testHashCodeEqual() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 6, 11, 14);
        assertHashCode(true, da, db);
    }

    public void testHashCodeNotEqual() {
        Difference da = new Difference(4, 6, 11, 14);
        Difference db = new Difference(4, 6, 11, 18);
        assertHashCode(false, da, db);
    }

    public void testToStringChange() {
        Difference d = new Difference(4, 6, 11, 14);
        assertEquals("[4, 6] => [11, 14]", d.toString());
    }

    public void testToStringAdd() {
        Difference d = new Difference(4, Difference.NONE, 11, 14);
        assertEquals("[4] => [11, 14]", d.toString());
    }

    public void testToStringDelete() {
        Difference d = new Difference(4, 6, 11, Difference.NONE);
        assertEquals("[4, 6] => [11]", d.toString());
    }

    public void testIsChange() {
        Difference d = new Difference(4, 6, 11, 14);
        assertEquals("d: " + d, true, d.isChange());
        assertEquals("d: " + d, false, d.isAdd());
        assertEquals("d: " + d, false, d.isDelete());
    }

    public void testIsAdd() {
        Difference d = new Difference(4, Difference.NONE, 11, 14);
        assertEquals("d: " + d, false, d.isChange());
        assertEquals("d: " + d, true, d.isAdd());
        assertEquals("d: " + d, false, d.isDelete());
    }

    public void testIsDelete() {
        Difference d = new Difference(4, 6, 11, Difference.NONE);
        assertEquals("d: " + d, false, d.isChange());
        assertEquals("d: " + d, false, d.isAdd());
        assertEquals("d: " + d, true, d.isDelete());
    }
}
