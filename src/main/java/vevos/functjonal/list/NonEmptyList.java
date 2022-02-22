package vevos.functjonal.list;

import java.util.Collection;
import java.util.List;

/**
 * A list that holds at least one element.
 * It does not allow removing elements once they are inserted.
 *
 * @param <T> Type of elements that are contained in this list.
 */
public class NonEmptyList<T> extends ListDecorator<T> {
    private final static String ERROR_MESSAGE = "Operation disallowed as it could make this list become empty!";

    public NonEmptyList(final List<T> list) {
        super(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Given list cannot be empty!");
        }
    }

    public T head() {
        return wrappee.get(0);
    }

    @Override
    public boolean remove(final Object o) {
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
    public T remove(final int index) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }
}
