package de.variantsync.functjonal.iteration;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Yield<T> implements Iterator<T>, Iterable<T> {
    private T focus = null;
    boolean focusVisited = true;
    private final Supplier<T> getNext;

    public Yield(final Supplier<T> getNext) {
        this.getNext = getNext;
    }

    public Yield(final Iterator<T> iterator) {
        this(() -> {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        });
    }

    public Yield(final Stream<T> stream) {
        this(stream.iterator());
    }

    void updateFocus() {
        if (focusVisited) {
            focus = getNext.get();
            focusVisited = false;
        }
    }

    @Override
    public boolean hasNext() {
        updateFocus();
        return focus != null;
    }

    @Override
    public T next() {
        updateFocus();
        focusVisited = true;
        return focus;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}