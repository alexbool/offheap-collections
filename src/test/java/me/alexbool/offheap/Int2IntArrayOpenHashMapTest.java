package me.alexbool.offheap;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class Int2IntArrayOpenHashMapTest {

    @Test
    public void test() {
        Int2IntArrayOpenHashMap map = new Int2IntArrayOpenHashMap(2, 10);
        map.put(1, new int[] { 1, 2, 3, 4 });
        Assert.assertEquals(1, map.size());
        map.put(2, new int[] { 5, 6, 7, 8, 9, 10 });
        Assert.assertEquals(2, map.size());
        Assert.assertArrayEquals(new int[] { 1, 2, 3, 4 }, map.get(1));
        Assert.assertArrayEquals(new int[] { 5, 6, 7, 8, 9, 10 }, map.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void reinsert() {
        Int2IntArrayOpenHashMap map = new Int2IntArrayOpenHashMap(2, 10);
        map.put(1, new int[] {});
        map.put(1, new int[] {});
    }

    @Test
    public void containsKey() {
        Int2IntArrayOpenHashMap map = new Int2IntArrayOpenHashMap(2, 10);
        Assert.assertFalse(map.containsKey(1));
        map.put(1, new int[] { 1, 2, 3, 4 });
        Assert.assertTrue(map.containsKey(1));
    }

    @Test
    public void bigMap() {
        final int mapSize = 500000;
        final int arraySize = 10;
        Int2IntArrayOpenHashMap map = new Int2IntArrayOpenHashMap(mapSize, mapSize * arraySize);
        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = i;
        }
        for (int i = 0; i < mapSize; i++) {
            map.put(i, array);
        }

        for (int i = 0; i < mapSize; i++) {
            Assert.assertArrayEquals(array, map.get(i));
        }
    }

    @Test
    public void putAll() {
        Int2IntArrayOpenHashMap map = new Int2IntArrayOpenHashMap(2, 10);
        Map<Integer, int[]> otherMap = new HashMap<>();

        otherMap.put(1, new int[] { 1, 2, 3, 4 });
        otherMap.put(2, new int[] { 5, 6, 7, 8, 9, 10 });

        map.putAll(otherMap);

        Assert.assertEquals(2, map.size());
        Assert.assertArrayEquals(new int[] { 1, 2, 3, 4 }, map.get(1));
        Assert.assertArrayEquals(new int[] { 5, 6, 7, 8, 9, 10 }, map.get(2));
    }
}
