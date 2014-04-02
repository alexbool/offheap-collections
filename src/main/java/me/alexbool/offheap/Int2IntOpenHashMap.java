package me.alexbool.offheap;

import it.unimi.dsi.fastutil.ints.AbstractInt2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.AbstractMap;
import java.util.Map;

/**
 * @author alexbool
 */
public class Int2IntOpenHashMap extends AbstractInt2SomethingOpenHashMap<Integer> implements Int2IntMap {

    private final IntBuffer values;
    private int defaultReturnValue = 0;

    public Int2IntOpenHashMap(int expected) {
        super(expected);
        this.values = ByteBuffer.allocateDirect(n * SIZE_OF_INT).asIntBuffer();
    }

    @Override
    public int put(int k, int v) {
        int pos = intHash(k);
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) {
                int oldValue = getValueAt(pos);
                setValueAt(pos, v);
                return oldValue;
            }
            pos = (pos + 1) & mask;
        }
        setUsedAt(pos);
        setKeyAt(pos, k);
        setValueAt(pos, v);
        size++;
        return defaultReturnValue;
    }

    @Override
    public Integer put(Integer key, Integer value) {
        return put(key.intValue(), value.intValue());
    }

    @Override
    public int get(int k) {
        int pos = intHash(k);
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) return getValueAt(pos);
            pos = (pos + 1) & mask;
        }
        return defaultReturnValue;
    }

    @Override
    public Integer get(Object key) {
        return get(((Integer) key).intValue());
    }

    @Override
    public int remove(int k) {
        int pos = intHash(k);
        while (isUsedAt(pos)) {
            if (getKeyAt(pos) == k) {
                int oldValue = getValueAt(pos);
                setNotUsedAt(pos);
                size--;
                return oldValue;
            }
            pos = (pos + 1) & mask;
        }
        return defaultReturnValue;
    }

    @Override
    public Integer remove(Object key) {
        return remove(((Integer) key).intValue());
    }

    @Override
    public boolean containsValue(int value) {
        for (int i = 0; i < values.capacity(); i++) {
            if (values.get(i) == value) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(((Integer) value).intValue());
    }

    @Override
    public void clear() {
        for (int i = 0; i < used.capacity(); i++) {
            used.put(i, (byte) 0);
        }
        this.size = 0;
    }

    @Override
    public IntSet keySet() {
        IntOpenHashSet result = new IntOpenHashSet(size);
        for (int i = 0; i < n; i++) {
            if (isUsedAt(i)) {
                result.add(getKeyAt(i));
            }
        }
        return result;
    }

    @Override
    public IntCollection values() {
        int[] result = new int[size];
        int j = 0;
        for (int i = 0; i < n; i++) {
            if (isUsedAt(i)) {
                result[j] = getValueAt(i);
                j++;
            }
        }
        return new IntArrayList(result);
    }

    @Override
    public ObjectSet<Map.Entry<Integer, Integer>> entrySet() {
        ObjectArraySet<Map.Entry<Integer, Integer>> result = new ObjectArraySet<>(size);
        for (int i = 0; i < n; i++) {
            if (isUsedAt(i)) {
                result.add(new AbstractMap.SimpleEntry<>(getKeyAt(i), getValueAt(i)));
            }
        }
        return result;
    }

    @Override
    public ObjectSet<Int2IntMap.Entry> int2IntEntrySet() {
        ObjectArraySet<Int2IntMap.Entry> result = new ObjectArraySet<>(size);
        for (int i = 0; i < n; i++) {
            if (isUsedAt(i)) {
                result.add(new AbstractInt2IntMap.BasicEntry(getKeyAt(i), getValueAt(i)));
            }
        }
        return result;
    }

    @Override
    public void defaultReturnValue(int i) {
        this.defaultReturnValue = i;
    }

    @Override
    public int defaultReturnValue() {
        return defaultReturnValue;
    }

    private int getValueAt(int pos) {
        return values.get(pos);
    }

    private void setValueAt(int pos, int value) {
        values.put(pos, value);
    }
}
