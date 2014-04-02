package me.alexbool.offheap;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import it.unimi.dsi.fastutil.HashCommon;

/**
 * @author alexbool
 */
public abstract class AbstractInt2SomethingOpenHashMap<T> implements Map<Integer, T> {

    protected static final int SIZE_OF_INT = 4;

    protected final IntBuffer keys;
    protected final ByteBuffer used;
    protected final float f;
    protected final int n;
    protected int size;
    protected int mask;

    protected AbstractInt2SomethingOpenHashMap(int expected) {
        this.f = 0.75f;
        this.n = HashCommon.arraySize(expected, f);
        this.mask = n - 1;
        this.keys = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.used = ByteBuffer.allocateDirect((int) Math.ceil((double) n / 8));
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof Integer) {
            return containsKey(((Integer) key).intValue());
        } else {
            return false;
        }
    }

    public boolean containsKey(int k) {
        // The starting point.
        int pos = intHash(k);
        // There's always an unused entry.
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) return true;
            pos = (pos + 1) & mask;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    protected boolean isUsedAt(int pos) {
        int index = pos / 8;
        byte bits = used.get(index);
        byte bitMask = (byte) (1 << (pos % 8));
        return (bits & bitMask) != 0;
    }

    protected void setUsedAt(int pos) {
        int index = pos / 8;
        byte bits = used.get(index);
        byte bitMask = (byte) (1 << (pos % 8));
        used.put(index, (byte) (bits | bitMask));
    }

    protected void setNotUsedAt(int pos) {
        int index = pos / 8;
        byte bits = used.get(index);
        byte bitMask = (byte) ~(1 << (pos % 8));
        used.put(index, (byte) (bits & bitMask));
    }

    protected int getKeyAt(int pos) {
        return keys.get(pos);
    }

    protected void setKeyAt(int pos, int key) {
        keys.put(pos, key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends T> m) {
        for (Map.Entry<? extends Integer, ? extends T> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    protected int intHash(int k) {
        return (HashCommon.murmurHash3((k) ^ mask)) & mask;
    }
}
