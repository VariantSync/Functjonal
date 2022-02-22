package vevos.functjonal.iteration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Groups subsequent iterated elements to lists of a given size.
 * Instead of iterating over elements individually, a ClusteredIterator collects the next n elements in a list
 * that will be returned, where n is a positive integer.
 * The last returned list might be shorter than n iff there were less than n elements remaining to visit.
 * @param <T>
 */
public class ClusteredIterator<T> implements Iterator<List<T>> {
    private final Iterator<T> inner;
    private final int clusterSize;

    /**
     * Clusters the elements returned by the inner iterator to chunks of the given size.
     */
    public ClusteredIterator(final Iterator<T> inner, int clusterSize) {
        this.inner = inner;
        this.clusterSize = clusterSize;
    }

    @Override
    public boolean hasNext() {
        return inner.hasNext();
    }

    @Override
    public List<T> next() {
        final List<T> cluster = new ArrayList<>(clusterSize);
        for (int i = 0; i < clusterSize && inner.hasNext(); ++i) {
            cluster.add(inner.next());
        }

        return cluster;
    }
}
