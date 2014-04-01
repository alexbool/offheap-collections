package me.alexbool.offheap;

import it.unimi.dsi.fastutil.HashCommon;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Int2IntArrayOpenHashMap implements Map<Integer, int[]> {

    protected static final int SIZE_OF_INT = 4;

    protected final IntBuffer keys;
    protected final IntBuffer valueOffsets;
    protected final IntBuffer valueSizes;
    protected final IntBuffer values;
    protected final ByteBuffer used;
    protected final float f;
    protected final int n;
    protected int size;
    protected transient int mask;
    protected int maxOffset;

    public Int2IntArrayOpenHashMap(int expected, int totalValues) {
        this.f = 0.75f;
        this.n = HashCommon.arraySize(expected, f);
        this.mask = n - 1;
        this.keys = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.valueOffsets = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.valueSizes = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.values = ByteBuffer.allocateDirect(totalValues * SIZE_OF_INT).asIntBuffer();
        this.used = ByteBuffer.allocateDirect((int) Math.ceil((double) n / (8 * SIZE_OF_INT)));
        this.size = 0;
        this.maxOffset = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public int[] get(Object key) {
        if (key instanceof Integer) {
            int intKey = (Integer) key;
            return get(intKey);
        } else {
            return null;
        }
    }

    public int[] get(int k) {
        // The starting point.
        int pos = (HashCommon.murmurHash3((k) ^ mask)) & mask;
        // There's always an unused entry.
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) return getValueAt(pos);
            pos = (pos + 1) & mask;
        }
        return null;
    }

    @Override
    public int[] put(Integer key, int[] value) {
        return put(key, value);
    }

    public int[] put(int k, int[] v) {
        // The starting point.
        int pos = (HashCommon.murmurHash3(k ^ mask)) & mask;
        // There's always an unused entry.
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) {
                throw new UnsupportedOperationException("Reinsertions are not supported");
            }
            pos = (pos + 1) & mask;
        }
        setUsedAt(pos);
        setKeyAt(pos, k);
        setValueAt(pos, v);
        size++;
        return null;
    }

    @Override
    public int[] remove(Object key) {
        throw new UnsupportedOperationException("Remove is not supported");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends int[]> m) {

    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Clear is not supported");
    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<int[]> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, int[]>> entrySet() {
        return null;
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

    protected int getKeyAt(int pos) {
        return keys.get(pos);
    }

    protected void setKeyAt(int pos, int key) {
        keys.put(pos, key);
    }

    protected int[] getValueAt(int pos) {
        int offset = valueOffsets.get(pos);
        int size = valueSizes.get(pos);
        int[] result = new int[size];
        IntBuffer duplicate = values.duplicate();
        duplicate.position(offset);
        duplicate.get(result);
        return result;
    }

    protected void setValueAt(int pos, int[] value) {
        int offset = maxOffset;
        valueOffsets.put(pos, offset);
        valueSizes.put(pos, value.length);
        IntBuffer duplicate = values.duplicate();
        duplicate.position(offset);
        duplicate.put(value);
        maxOffset += value.length;
    }
}
