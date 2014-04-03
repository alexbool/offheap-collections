package me.alexbool.offheap;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author alexbool
 */
public class Int2IntOpenHashMapTest {

    @Test
    public void test() {
        Int2IntOpenHashMap map = new Int2IntOpenHashMap(10);
        for (int i = 0; i < 10; i++) {
            map.put(i, i + 1);
        }

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(i + 1, map.get(i));
        }
    }

    @Test
    public void containsKey() {
        Int2IntOpenHashMap map = new Int2IntOpenHashMap(2);
        Assert.assertFalse(map.containsKey(1));
        map.put(1, 200);
        Assert.assertTrue(map.containsKey(1));
    }

    @Test
    public void bigMap() {
        final int mapSize = 500000;
        Int2IntOpenHashMap map = new Int2IntOpenHashMap(mapSize);
        for (int i = 0; i < mapSize; i++) {
            map.put(i, i * 2);
        }

        for (int i = 0; i < mapSize; i++) {
            Assert.assertEquals(i * 2, map.get(i));
        }
    }

    @Test
    public void putAll() {
        Int2IntOpenHashMap map = new Int2IntOpenHashMap(2);
        Map<Integer, Integer> otherMap = new HashMap<>();

        otherMap.put(1, 10);
        otherMap.put(2, 20);

        map.putAll(otherMap);

        Assert.assertEquals(2, map.size());
        Assert.assertEquals(10, map.get(1));
        Assert.assertEquals(20, map.get(2));
    }
}
