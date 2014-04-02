package me.alexbool.offheap;

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
}
