package me.alexbool.offheap;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Set;

import it.unimi.dsi.fastutil.HashCommon;

public class Int2IntArrayOpenHashMap extends AbstractInt2SomethingOpenHashMap<int[]> {

    protected final IntBuffer valueOffsets;
    protected final IntBuffer valueSizes;
    protected final IntBuffer values;
    protected int maxOffset;

    public Int2IntArrayOpenHashMap(int expected, int totalValues) {
        super(expected);
        this.valueOffsets = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.valueSizes = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
        this.values = ByteBuffer.allocateDirect(totalValues * SIZE_OF_INT).asIntBuffer();
        this.maxOffset = 0;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("ContainsValue is not supported");
    }

    @Override
    public int[] get(Object key) {
        if (key instanceof Integer) {
            return get(((Integer) key).intValue());
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
        return put(key.intValue(), value);
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
    public void clear() {
        throw new UnsupportedOperationException("Clear is not supported");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet is not supported");
    }

    @Override
    public Collection<int[]> values() {
        throw new UnsupportedOperationException("values is not supported");
    }

    @Override
    public Set<Entry<Integer, int[]>> entrySet() {
        throw new UnsupportedOperationException("entrySet is not supported");
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
