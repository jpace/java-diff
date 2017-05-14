package org.incava.diff;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;

import static org.incava.diff.Util.list;

public class TestDiff extends TestCase {
    public TestDiff(String name) {
        super(name);
    }

    public Difference newDiff(Integer delStart, Integer delEnd, Integer addStart, Integer addEnd) {
        return new Difference(delStart, delEnd, addStart, addEnd);
    }

    public List<Difference> diffList(Difference ... diffs) {
        return Arrays.asList(diffs);
    }

    public void testStrings() {
        List<String> a = list("a", "b", "c",      "e", "h", "j",      "l", "m", "n", "p");
        List<String> b = list(     "b", "c", "d", "e", "f", "j", "k", "l", "m", "r", "s", "t");

        Difference[] expected = new Difference[] {
            newDiff(0,  0,  0, -1),
            newDiff(3, -1,  2,  2),
            newDiff(4,  4,  4,  4),
            newDiff(6, -1,  6,  6),
            newDiff(8,  9,  9, 11),
        };

        runDiff(a, b, expected);
    }

    public void testAddEnd() {
        List<String> a = list("a", "b", "c", "d");
        List<String> b = list(          "c", "d");

        Difference[] expected = new Difference[] {
            newDiff(0, 1, 0, -1),
        };

        runDiff(a, b, expected);
    }

    public void testAddEndAsArrays() {
        String[] a = new String[] { "a", "b", "c", "d" };
        String[] b = new String[] {           "c", "d" };

        Difference[] expected = new Difference[] {
            newDiff(0, 1, 0, -1),
        };

        runDiff(a, b, expected);
    }

    // Tests that the legacy <code>diff()</code> method works. That method has
    // been deprecated, and replace by <code>execute()</code>.
    public void testAddEndDiffMethod() {
        List<String> a = list("a", "b", "c", "d");
        List<String> b = list(          "c", "d");

        Difference[] expected = new Difference[] {
            newDiff(0, 1, 0, -1),
        };

        runDiffWithDiffMethod(a, b, expected);
    }

    public void testAddStartAddEnd() {
        List<String> a = list("a", "b", "c", "d", "x", "y", "z");
        List<String> b = list(          "c", "d");

        Difference[] expected = new Difference[] {
            newDiff(0,  1,  0, -1),
            newDiff(4,  6,  2, -1)
        };

        runDiff(a, b, expected);
    }

    public void testStrings4() {
        List<String> a = list("a",           "b", "c",      "d", "e");
        List<String> b = list("a", "x", "y", "b", "c", "j", "e");

        Difference[] expected = new Difference[] {
            newDiff(1, -1,  1,  2),
            newDiff(3,  3,  5,  5),
        };

        runDiff(a, b, expected);
    }

    public void testIntegerAddStart() {
        Object[] a = new Integer[] {
            new Integer(1), 
            new Integer(2), 
            new Integer(3), 
        };

        Object[] b = new Integer[] {
            new Integer(2), 
            new Integer(3), 
        };

        Difference[] expected = new Difference[] {
            newDiff(0, 0, 0, -1)
        };

        runDiff(a, b, expected);
    }

    public NoncomparableObject newNoncmpObj(String s) {
        return new NoncomparableObject(s);
    }

    public void testComparator() {
        NoncomparableObject[] a = new NoncomparableObject[] {
            newNoncmpObj("a"), 
            newNoncmpObj("b"), 
            newNoncmpObj("c"), 
            newNoncmpObj("g"), 
            newNoncmpObj("h1"), 
        };

        NoncomparableObject[] b = new NoncomparableObject[] {
            newNoncmpObj("b"), 
            newNoncmpObj("c"), 
            newNoncmpObj("d"), 
            newNoncmpObj("e"), 
            newNoncmpObj("f"), 
            newNoncmpObj("g"), 
            newNoncmpObj("h2"), 
        };

        Difference[] expected = new Difference[] {
            newDiff(0,  0,  0, -1),
            newDiff(3, -1 , 2,  4),
            newDiff(4,  4,  6,  6),
        };

        Comparator<NoncomparableObject> comparator = new NoncomparableObject.NoncomparableObjectComparator();
        
        runDiff(new Diff<NoncomparableObject>(a, b, comparator), expected);
    }

    public ComparableObject newCmpObj(String s) {
        return new ComparableObject(s);
    }

    public void testComparableObject() {
        Object[] a = new ComparableObject[] {
            newCmpObj("a"), 
            newCmpObj("b"), 
            newCmpObj("c"), 
            newCmpObj("g"), 
            newCmpObj("h1"), 
        };

        Object[] b = new ComparableObject[] {
            newCmpObj("b"), 
            newCmpObj("c"), 
            newCmpObj("d"), 
            newCmpObj("e"), 
            newCmpObj("f"), 
            newCmpObj("g"), 
            newCmpObj("h2"), 
        };

        Difference[] expected = new Difference[] {
            newDiff(0,  0,  0, -1),
            newDiff(3, -1,  2,  4),
            newDiff(4,  4,  6,  6),
        };

        runDiff(a, b, expected);
    }
    
    public void testLongArray() {
        Object[] a = new Object[] {
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
        };

        Object[] b = new Object[] {
            "a",
            "b",
            "p",                // insert
            "q",                // ...
            "r",                // ...
            "s",                // ...
            "t",                // ...
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "u",                // change
            "l",
        };

        Difference[] expected = new Difference[] {
            newDiff(2,  -1,  2,  6),
            newDiff(10, 10, 15, 15),
        };

        runDiff(a, b, expected);
    }

    public void testRepeated() {
        Object[] a = new Object[] {
            "a",
            "a",
            "a",
            "a",
            "b",
            "b",
            "b",
            "a",
            "a",
            "a",
            "a",
            "b",
            "b",
            "b",
            "a",
            "a",
            "a",
            "a",
            "b",
            "b",
            "b",
            "a",
            "a",
            "a",
            "a",
            "b",
            "b",
            "b",
        };

        Object[] b = new Object[] {
            "a",
            "a",
            "a",
            "a",
            "b",
            "b",
            "b",
            "a",
            "b",
            "b",
            "b",
            "a",
            "a",
            "a",
            "a",
        };

        Difference[] expected = new Difference[] {
            newDiff(8,  10,  8, -1),
            newDiff(18, 27, 15, -1),
        };

        runDiff(a, b, expected);
    }

    public void testReallyBig() {
        Object[] a = new Object[] {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "A",
            "H",
            "I",
            "J",
            "D",
            "K",
            "L",
            "C",
            "G",
            "M",
            "H",
            "N",
            "J",
            "I",
            "K",
            "O",
            "C",
            "G",
            "M",
            "P",
            "Q",
            "J",
            "R",
            "K",
            "S",
            "C",
            "C",
            "F",
            "G",
            "D",
            "T",
            "N",
            "G",
            "M",
            "U",
            "V",
            "J",
            "Q",
            "K",
            "W",
            "C",
            "G",
            "M",
            "X",
            "C",
            "V",
            "K",
            "Y",
            "C",
            "G",
            "G",
            "A",
            "Z",
            "AA",
            "J",
            "C",
            "Z",
            "G",
            "V",
            "K",
            "BB",
            "C",
            "G",
            "M",
            "CC",
            "DD",
            "J",
            "EE",
            "K",
            "FF",
            "C",
            "AA",
            "G",
            "M",
            "GG",
            "K",
            "HH",
            "C",
            "DD",
            "G",
            "M",
            "II",
            "II",
            "II",
        };

        Object[] b = new Object[] {
            "A",
            "B",
            "C",
            "JJ",
            "G",
            "A",
            "II",
            "KK",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "A",
            "H",
            "I",
            "J",
            "D",
            "K",
            "L",
            "C",
            "G",
            "M",
            "H",
            "N",
            "J",
            "I",
            "K",
            "O",
            "C",
            "G",
            "M",
            "P",
            "Q",
            "J",
            "R",
            "K",
            "S",
            "C",
            "C",
            "F",
            "G",
            "D",
            "T",
            "N",
            "G",
            "M",
            "U",
            "V",
            "J",
            "Q",
            "K",
            "W",
            "C",
            "G",
            "M",
            "X",
            "C",
            "V",
            "K",
            "Y",
            "C",
            "G",
            "G",
            "A",
            "Z",
            "AA",
            "J",
            "C",
            "Z",
            "G",
            "V",
            "K",
            "BB",
            "C",
            "G",
            "M",
            "CC",
            "DD",
            "J",
            "EE",
            "K",
            "FF",
            "C",
            "AA",
            "G",
            "M",
            "GG",
            "K",
            "HH",
            "C",
            "DD",
            "G",
            "M",
            "II",
            "II",
            "II",
            "II",
        };

        Difference[] expected = new Difference[] {
            newDiff(3,  -1,  3, 10),
            newDiff(88, -1, 96, 96),
        };

        runDiff(a, b, expected);
    }

    // This test doesn't yet work. The problem is that LCSes are logically
    // incorrect, with regard to source code. That is, after a failed mapping
    // (i.e., null), the "next" LCS is the *previous* LCS, not the current one,
    // that is, starting at the next element after the null. Not sure when this
    // is an artifact of the algorithm, or whether it is "correct" and my
    // expectations aren't.

    public void misleading_diffs_testFromZipDiff() {
        Object[] a = new Object[] {
            "{",
            "ZipEntry",
            "e",
            "=",
            "entry",
            ";",
            "if",
            "(",
            "e",
            "!=",
            "null",
            ")",
            "{",
            "switch",
            "(",
            "e",
            ".",
            "method",
            ")",
            "{",
            "case",
            "DEFLATED",
            ":",
            "if",
            "(",
            "(",
            "e",
            ".",
            "flag",
            "&",
            "8",
            ")",
            "==",
            "0",
            ")",
            "{",
            "if",
            "(",
            "e",
            ".",
            "size",
            "!=",
            "def",
            ".",
            "getTotalIn",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry size (expected \"",
            "+",
            "e",
            ".",
            "size",
            "+",
            "\" but got \"",
            "+",
            "def",
            ".",
            "getTotalIn",
            "(",
            ")",
            "+",
            "\" bytes)\"",
            ")",
            ";",
            "}",
            "if",
            "(",
            "e",
            ".",
            "csize",
            "!=",
            "def",
            ".",
            "getTotalOut",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry compressed size (expected \"",
            "+",
            "e",
            ".",
            "csize",
            "+",
            "\" but got \"",
            "+",
            "def",
            ".",
            "getTotalOut",
            "(",
            ")",
            "+",
            "\" bytes)\"",
            ")",
            ";",
            "}",
            "if",
            "(",
            "e",
            ".",
            "crc",
            "!=",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry CRC-32 (expected 0x\"",
            "+",
            "Long",
            ".",
            "toHexString",
            "(",
            "e",
            ".",
            "crc",
            ")",
            "+",
            "\" but got 0x\"",
            "+",
            "Long",
            ".",
            "toHexString",
            "(",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ")",
            "+",
            "\")\"",
            ")",
            ";",
            "}",
            "}",
            "else",
            "{",
            "e",
            ".",
            "size",
            "=",
            "def",
            ".",
            "getTotalIn",
            "(",
            ")",
            ";",
            "e",
            ".",
            "csize",
            "=",
            "def",
            ".",
            "getTotalOut",
            "(",
            ")",
            ";",
            "e",
            ".",
            "crc",
            "=",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ";",
            "writeEXT",
            "(",
            "e",
            ")",
            ";",
            "}",
            "def",
            ".",
            "reset",
            "(",
            ")",
            ";",
            "written",
            "+=",
            "e",
            ".",
            "csize",
            ";",
            "break",
            ";",
            "}",
            "}",
            "}",
        };

        Object[] b = new Object[] {
            "{",
            "ZipEntry",
            "e",
            "=",
            "entry",
            ";",
            "if",
            "(",
            "e",
            "!=",
            "null",
            ")",
            "{",
            "switch",
            "(",
            "e",
            ".",
            "method",
            ")",
            "{",
            "case",
            "DEFLATED",
            ":",
            "if",
            "(",
            "(",
            "e",
            ".",
            "flag",
            "&",
            "8",
            ")",
            "==",
            "0",
            ")",
            "{",
            "if",
            "(",
            "e",
            ".",
            "size",
            "!=",
            "def",
            ".",
            "getBytesRead",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry size (expected \"",
            "+",
            "e",
            ".",
            "size",
            "+",
            "\" but got \"",
            "+",
            "def",
            ".",
            "getBytesRead",
            "(",
            ")",
            "+",
            "\" bytes)\"",
            ")",
            ";",
            "}",
            "if",
            "(",
            "e",
            ".",
            "csize",
            "!=",
            "def",
            ".",
            "getBytesWritten",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry compressed size (expected \"",
            "+",
            "e",
            ".",
            "csize",
            "+",
            "\" but got \"",
            "+",
            "def",
            ".",
            "getBytesWritten",
            "(",
            ")",
            "+",
            "\" bytes)\"",
            ")",
            ";",
            "}",
            "if",
            "(",
            "e",
            ".",
            "crc",
            "!=",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ")",
            "{",
            "throw",
            "new",
            "ZipException",
            "(",
            "\"invalid entry CRC-32 (expected 0x\"",
            "+",
            "Long",
            ".",
            "toHexString",
            "(",
            "e",
            ".",
            "crc",
            ")",
            "+",
            "\" but got 0x\"",
            "+",
            "Long",
            ".",
            "toHexString",
            "(",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ")",
            "+",
            "\")\"",
            ")",
            ";",
            "}",
            "}",
            "else",
            "{",
            "e",
            ".",
            "size",
            "=",
            "def",
            ".",
            "getBytesRead",
            "(",
            ")",
            ";",
            "e",
            ".",
            "csize",
            "=",
            "def",
            ".",
            "getBytesWritten",
            "(",
            ")",
            ";",
            "e",
            ".",
            "crc",
            "=",
            "crc",
            ".",
            "getValue",
            "(",
            ")",
            ";",
            "writeEXT",
            "(",
            "e",
            ")",
            ";",
            "}",
            "def",
            ".",
            "reset",
            "(",
            ")",
            ";",
            "written",
            "+=",
            "e",
            ".",
            "csize",
            ";",
            "break",
            ";",
            "}",
            "}",
            "}",
        };
        
        Difference[] expected = new Difference[] {
            new Difference(3,  -1,  3, 10),
            new Difference(88, -1, 96, 96),
        };

        runDiff(a, b, expected);
    }

    public void testBlanks() {
        // many thanks to Oliver Koll for finding and submitting this.

        Object[] a = new Object[] {
            "same",
            "same",
            "same",
            "",
            "same",
            "del",      // delete
            "",         // ...
            "del"       // ...
        };

        Object[] b = new Object[] {
            "ins",      // insert
            "",         // ...
            "same",
            "same",
            "same",
            "",
            "same"
        };

        Difference[] expected = new Difference[] {
            newDiff(0, -1,  0,  1),
            newDiff(5,  7,  7, -1),
        };

        runDiff(a, b, expected);
    }
    
    public void assertDifferences(List<Difference> differences, Difference[] expected) {
        assertEquals("length of output", expected.length, differences.size());
        for (int ei = 0; ei < expected.length; ++ei) {
            Difference actual = differences.get(ei);
            Difference exp = expected[ei];
            assertEquals("expected[" + ei +"]", exp, actual);
        }
    }

    protected <T> void runDiff(Diff<T> diff, Difference[] expected) {
        List<Difference> differences = diff.execute();
        assertDifferences(differences, expected);
    }    
    
    @SuppressWarnings("deprecation")
    protected void runDiffWithDiffMethod(Object[] a, Object[] b, Difference[] expected) {
        Diff<Object> diff = new Diff<Object>(a, b);
        List<Difference> differences = diff.diff();
        assertDifferences(differences, expected);
    }
    
    protected void runDiff(Object[] a, Object[] b, Difference[] expected) {
        Diff<Object> diff = new Diff<Object>(a, b);
        List<Difference> differences = diff.execute();
        assertDifferences(differences, expected);
    }

    @SuppressWarnings("deprecation")
    protected <T> void runDiffWithDiffMethod(List<T> a, List<T> b, Difference[] expected) {
        Diff<T> diff = new Diff<T>(a, b);
        List<Difference> differences = diff.diff();
        assertDifferences(differences, expected);
    }
    
    protected <T> void runDiff(List<T> a, List<T> b, Difference[] expected) {
        Diff<T> diff = new Diff<T>(a, b);
        List<Difference> differences = diff.execute();
        assertDifferences(differences, expected);
    }
}
