package me.alexbool.offheap.util;

import java.util.Arrays;
import java.util.Collection;

import it.unimi.dsi.fastutil.ints.AbstractIntSet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntIterators;

/**
 * Read-only set that wraps sorted int array. This way it achieves O(log n) performance on contains().
 * If you provide unsorted int array, or int array with duplicates, results are undefined.
 *
 * @author alexbool
 */
public class UnmodifiableSortedIntArraySet extends AbstractIntSet {
    private final int[] values;

    public UnmodifiableSortedIntArraySet(int[] values) {
        this.values = values;
    }

    @Override
    public boolean contains(int i) {
        return Arrays.binarySearch(values, i) >= 0;
    }

    @Override
    public IntIterator iterator() {
        return IntIterators.wrap(values);
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public int[] toIntArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public boolean remove(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean rem(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(IntCollection integers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(IntCollection integers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(IntCollection integers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
