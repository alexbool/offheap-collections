package me.alexbool.offheap.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author alexbool
 */
public class UnmodifiableSortedIntArraySetTest {

    @Test
    public void contains() {
        int[] values = new int[] { 0, 1, 3, 5, 13, 34, 45, 56, 300 };
        UnmodifiableSortedIntArraySet set = new UnmodifiableSortedIntArraySet(values);

        Assert.assertTrue(set.contains(0));
        Assert.assertTrue(set.contains(1));
        Assert.assertTrue(set.contains(3));
        Assert.assertTrue(set.contains(5));
        Assert.assertTrue(set.contains(13));
        Assert.assertTrue(set.contains(34));
        Assert.assertTrue(set.contains(45));
        Assert.assertTrue(set.contains(56));
        Assert.assertTrue(set.contains(300));

        Assert.assertFalse(set.contains(2));
        Assert.assertFalse(set.contains(4));
        Assert.assertFalse(set.contains(6));
        Assert.assertFalse(set.contains(301));
    }
}
