package org.variantsync.functjonal.list;

import java.util.Collection;
import java.util.List;

public class ImmutableList<T> extends ListDecorator<T> {
    private final static String ERROR_MESSAGE = "List is immutable.";

    public ImmutableList(final List<T> list) {
        super(list);
    }

    @Override
    public boolean add(final T t) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public T set(final int index, final T element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public T remove(final int index) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }
}
