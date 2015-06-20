package org.incava.diff;

import java.util.List;
import junit.framework.TestCase;
import static org.incava.diff.IUtil.list;

public class DifferTest extends TestCase {
    public static class TestAddDifference extends Difference {
        public TestAddDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
            super(delStart, delEnd, addStart, addEnd);
        }
    }

    public static class TestChangeDifference extends Difference {
        public TestChangeDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
            super(delStart, delEnd, addStart, addEnd);
        }
    }

    public static class TestDeleteDifference extends Difference {
        public TestDeleteDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
            super(delStart, delEnd, addStart, addEnd);
        }
    }

    public DifferTest(String name) {
        super(name);
    }

    public void assertIsAdd(Difference ... differences) {
        for (Difference diff : differences) {
            assertTrue("diff: " + diff, diff.isAdd());
            assertInstanceOf(diff, TestAddDifference.class);
        }
    }

    public void assertIsChange(Difference ... differences) {
        for (Difference diff : differences) {
            assertTrue("diff: " + diff, diff.isChange());
            assertInstanceOf(diff, TestChangeDifference.class);
        }
    }

    public void assertIsDelete(Difference ... differences) {
        for (Difference diff : differences) {
            assertTrue("diff: " + diff, diff.isDelete());
            assertInstanceOf(diff, TestDeleteDifference.class);
        }
    }

    public void assertInstanceOf(Difference diff, Class<? extends Difference> cls) {
        assertTrue("diff.class: " + diff.getClass() + " instanceof " + cls, diff.getClass().equals(cls));
    }

    public void testDeleteOneAtStart() {
        //                    del       
        List<String> a = list("a", "b");
        List<String> b = list(     "b");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 0, 0, Difference.NONE),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsDelete(differences.toArray(new Difference[0]));
    }

    public void testDeleteTwoAtStart() {
        //                    del       
        List<String> a = list("a", "b", "c");
        List<String> b = list(          "c");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 1, 0, Difference.NONE),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsDelete(differences.toArray(new Difference[0]));
    }

    public void testDeleteThreeAtStart() {
        //                    del       
        List<String> a = list("a", "b", "c", "d");
        List<String> b = list(               "d");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 2, 0, Difference.NONE),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsDelete(differences.toArray(new Difference[0]));
    }

    public void testAddOneAtStart() {
        //                    add       
        List<String> a = list(     "b");
        List<String> b = list("a", "b");
        
        Difference[] expected = new Difference[] {
            new Difference(0, Difference.NONE,  0, 0),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsAdd(differences.toArray(new Difference[0]));
    }

    public void testAddTwoAtStart() {
        //                    add       
        List<String> a = list(          "c");
        List<String> b = list("a", "b", "c");
        
        Difference[] expected = new Difference[] {
            new Difference(0, Difference.NONE, 0, 1),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsAdd(differences.toArray(new Difference[0]));
    }

    public void testAddThreeAtStart() {
        //                    add
        List<String> a = list(               "d");
        List<String> b = list("a", "b", "c", "d");
        
        Difference[] expected = new Difference[] {
            new Difference(0, Difference.NONE,  0, 2),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsAdd(differences.toArray(new Difference[0]));
    }

    public void testChangeOneAtStart() {
        //                    chg
        List<String> a = list("a", "b");
        List<String> b = list("x", "b");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 0,  0, 0),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsChange(differences.toArray(new Difference[0]));
    }

    public void testChangeTwoAtStart() {
        //                    chg       
        List<String> a = list("a", "b", "c");
        List<String> b = list("x", "y", "c");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 1, 0, 1),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsChange(differences.toArray(new Difference[0]));
    }

    public void testChangeThreeAtStart() {
        //                    chg
        List<String> a = list("a", "b", "c", "d");
        List<String> b = list("x", "y", "z", "d");
        
        Difference[] expected = new Difference[] {
            new Difference(0, 2,  0, 2),
        };

        List<Difference> differences = assertDifferences(a, b, expected);
        assertIsChange(differences.toArray(new Difference[0]));
    }

    public void testStringsAtLastFrom() {
        //                    del            add       chg       add       chg..........
        List<String> a = list("a", "b", "c",      "e", "h", "j",      "l", "m", "n", "p");
        List<String> b = list(     "b", "c", "d", "e", "f", "j", "k", "l", "m", "r", "s", "t");
        
        Difference[] expected = new Difference[] {
            new Difference(0,                   0,  0, Difference.NONE),
            new Difference(3, Difference.NONE,  2,  2),
            new Difference(4,                   4,  4,  4),
            new Difference(6, Difference.NONE,  6,  6),
            new Difference(8, 9,                9, 11),
        };

        assertDifferences(a, b, expected);
    }

    public void testStringsAtLastTo() {
        //                    del            add       chg       add       chg..........
        List<String> a = list("a", "b", "c",      "e", "h", "j",      "l", "m", "n", "p", "aa", "bb", "cc", "dd");
        List<String> b = list(     "b", "c", "d", "e", "f", "j", "k", "l", "m", "r", "s", "t");
        
        Difference[] expected = new Difference[] {
            new Difference(0,                   0,  0, Difference.NONE),
            new Difference(3, Difference.NONE,  2,  2),
            new Difference(4,                   4,  4,  4),
            new Difference(6, Difference.NONE,  6,  6),
            new Difference(8, 13,               9, 11),
        };

        assertDifferences(a, b, expected);
    }

    protected <T> List<Difference> assertDifferences(List<T> a, List<T> b, Difference[] expected) {
        Diff<T> diff = new Diff<T>(a, b) {
            public Difference createDifference(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
                if (delEnd == Difference.NONE) {
                    return new TestAddDifference(delStart, delEnd, addStart, addEnd);
                }
                else if (addEnd == Difference.NONE) {
                    return new TestDeleteDifference(delStart, delEnd, addStart, addEnd);
                }
                else {
                    return new TestChangeDifference(delStart, delEnd, addStart, addEnd);
                }
            }
        };

        List<Difference> differences = diff.execute();

        int count = Math.max(expected.length, differences.size());
        
        for (int idx = 0; idx < count; ++idx) {
            Difference actual = idx >= differences.size() ? null : differences.get(idx);
            Difference exp = idx >= expected.length ? null : expected[idx];
            assertEquals("expected[" + idx +"]", exp, actual);
        }

        return differences;
    }
}
