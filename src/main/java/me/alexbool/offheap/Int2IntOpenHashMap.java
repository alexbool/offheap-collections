package me.alexbool.offheap;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Set;

/**
 * @author alexbool
 */
public class Int2IntOpenHashMap extends AbstractInt2SomethingOpenHashMap<Integer> {

    private final IntBuffer values;

    public Int2IntOpenHashMap(int expected) {
        super(expected);
        this.values = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Integer get(Object key) {
        return null;
    }

    @Override
    public Integer put(Integer key, Integer value) {
        return null;
    }

    @Override
    public Integer remove(Object key) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<Integer> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        return null;
    }
}
