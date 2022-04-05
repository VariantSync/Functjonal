package org.variantsync.functjonal.iteration;

import java.util.Iterator;
import java.util.function.Function;

/**
 * An iterator to transform the value type of an iterator.
 * A MappedIterator wraps an iterator and transforms each visited element using pre-defined function.
 * @param <A> The type of elements to iterate over.
 * @param <B> The type of elements to return.
 */
public class MappedIterator<A, B> implements Iterator<B> {
    private final Iterator<A> as;
    private final Function<A, B> map;

    /**
     * Wraps the given iterator in a MappedIterator and applies the given function to each iterated element.
     */
    public MappedIterator(final Iterator<A> iterator, final Function<A, B> map) {
        this.as = iterator;
        this.map = map;
    }

    @Override
    public boolean hasNext() {
        return as.hasNext();
    }

    @Override
    public B next() {
        return map.apply(as.next());
    }
}
