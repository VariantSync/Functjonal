package de.variantsync.functjonal.list;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

/**
 * A view that separates a list into head (first element) and tail (remaining elements).
 * A ListHeadTailView thus shows a sublist of the given list starting at a certain index and ending at the end of
 * the viewed list.
 */
public class ListHeadTailView<T> extends ListDecorator<T> {
    private final int headIndex;

    public ListHeadTailView(final List<T> list) {
        this(list, 0);
    }

    public ListHeadTailView(final List<T> list, final int headIndex) {
        super(list);
        this.headIndex = headIndex;
    }

    public boolean empty() {
        return headIndex >= size();
    }

    public T head() {
        return get(headIndex);
    }

    public Optional<T> safehead() {
        if (empty()) {
            return Optional.empty();
        }

        return Optional.of(head());
    }

    public ListHeadTailView<T> tail() {
        return new ListHeadTailView<>(wrappee, headIndex + 1);
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        int jumpsToDo = headIndex + index;
        final ListIterator<T> i = wrappee.listIterator();
        while (jumpsToDo > 0) {
            i.next();
            --jumpsToDo;
        }
        return i;
    }
}
