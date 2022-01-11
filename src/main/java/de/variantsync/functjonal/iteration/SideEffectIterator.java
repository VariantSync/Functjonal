package de.variantsync.functjonal.iteration;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * An iterator that wraps another iterator and performs a side-effect on each element before returning it.
 */
public class SideEffectIterator<T> implements Iterator<T> {
    private final Iterator<T> inner;
    private final Consumer<T> effect;

    /**
     * Wraps the given iterator and performs the given effect before returning each element.
     */
    public SideEffectIterator(final Iterator<T> inner, final Consumer<T> effect) {
        this.inner = inner;
        this.effect = effect;
    }

    @Override
    public boolean hasNext() {
        return inner.hasNext();
    }

    @Override
    public T next() {
        final T n = inner.next();
        effect.accept(n);
        return n;
    }
}
