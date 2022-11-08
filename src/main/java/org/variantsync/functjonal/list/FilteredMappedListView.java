package org.variantsync.functjonal.list;

import java.util.AbstractSequentialList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Unmodifiable view of a list where the elements are filtered using a predicate and transformed
 * using a mapping function.
 *
 * <p>Modifications to the backing list are directly visible to this list, though all iterators are
 * invalid as soon a modification occurs (regardless of what additional promises the underlying list
 * implementation makes).
 *
 * <p>This view only provides sequential access to the underlying list (to make changes to the
 * backing list visible immediately). This implies that all random access methods and {@link size}
 * run in linear time, therefore specialized functions like {@code isEmpty} and iteration over the
 * list should be preferred.
 *
 * <p>The filter and map function provided to this class may be called at arbitrary times and for an
 * arbitrary number of times for each element in the backing list. To get predictable behaviour and
 * allow future modifications to this implementation, the filter and map function should therefore
 * be a pure function: It should have no side effects and return the same result given the same
 * element. What <i>the same</i> means (identity, equality or something different) is up to the user
 * of this class, as this implementation doesn't make any assumption about this.
 *
 * @param <Orig> the element type of the backing list (the input to the filter and the
 * transformer).
 * @param <New> the element type of the view (the output of the transformer)
 *
 * @author Benjamin Moosherr
 */
public class FilteredMappedListView<Orig, New> extends AbstractSequentialList<New> {
    /**
     * The list whose elements are filtered and mapped to be viewed.
     */
    private final List<Orig> backingList;

    /**
     * The filter and map functions.
     * This function is applied to each element in {@link backingList}. All elements which result in
     * {@link Optional#empty} are not shown in this view. In contrast, elements for which this
     * function returns an {@link Optional#of} are shown as the {@link Optional.get value} of this
     * optional.
     */
    private final Function<Orig, Optional<New>> filterMap;

    private FilteredMappedListView(List<Orig> backingList, Function<Orig, Optional<New>> filterMap) {
        this.backingList = backingList;
        this.filterMap = filterMap;
    }

    /**
     * Creates an unmodifiable view of the elements of {@code list} which satisfy
     * {@code predicate}.
     */
    public static <E> FilteredMappedListView<E, E> filter(List<E> list, Predicate<E> predicate) {
        return new FilteredMappedListView<>(list, (element) -> {
            if (predicate.test(element)) {
                return Optional.of(element);
            } else {
                return Optional.empty();
            }
        });
    }

    /**
     * Creates an unmodifiable view of the results of {@code mapper} applied to all elements of
     * {@code list}
     */
    public static <Orig, New> FilteredMappedListView<Orig, New> map(List<Orig> list, Function<Orig, New> mapper) {
        return new FilteredMappedListView<>(list, (element) -> Optional.of(mapper.apply(element)));
    }

    /**
     * Creates an unmodifiable view of the present results of {@code mapper} applied to all
     * elements of {@code list}
     */
    public static <Orig, New> FilteredMappedListView<Orig, New> filterMap(List<Orig> list, Function<Orig, Optional<New>> filterMap) {
        return new FilteredMappedListView<>(list, filterMap);
    }

    private class FilterMapListIterator implements ListIterator<New> {
        /**
         * The iterator over the backing list at the position corresponding to the current position
         * of this iterator.
         */
        private ListIterator<Orig> backingIterator;

        /**
         * A cache for the element which will be returned by {@link next()}.
         * This may be {@code null}. In this case it has to be retrieved using {@link retrieveNext}.
         *
         * <p>This is mainly used to optimize the most common usage pattern:
         * <code>
         *   while (iterator.hasNext()) {
         *     var element = iterator.next();
         *     // Do something with `element`
         *   }
         * </code>
         *
         * If {@link next()} is called after {@link previous()}, the returned elements will be the
         * same. This behaviour is also cached because it doesn't add extra costs.
         */
        private New next;

        /**
         * A cache for the element which will be returned by {@link previous()}.
         *
         * See {@link next} for details as this is symmetrical (just <i>next</i> and <i>previous</i>
         * are swapped).
         */
        private New previous;

        /**
         * The index of the element returned returned by {@link next()}.
         * This is not the same as {@code backingIterator.nextIndex()} as it doesn't count elements
         * for which {@link filterMap} returns {@link Optional#empty}.
         */
        int nextIndex;

        /**
         * Creates a new iterator backed by {@code iterator} and moves to the index {@code index}.
         * The given iterator has to be at the start ({@code iterator.nextIndex()} has to be 0).
         */
        public FilterMapListIterator(ListIterator<Orig> iterator, int index) {
            this.backingIterator = iterator;
            this.nextIndex = index;
            this.next = null;
            this.previous = null;
            this.nextIndex = 0;

            for (int i = 0; i < index; i++) {
                next();
            }
        }

        @Override
        public boolean hasNext() {
            if (next == null) {
                next = retrieveNext();
            }

            return next != null;
        }

        @Override
        public boolean hasPrevious() {
            if (previous == null) {
                previous = retrievePrevious();
            }

            return previous != null;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public New next() {
            // Is the element cached?
            if (next == null) {
                next = retrieveNext();
            }

            // Is there a *next* element?
            if (next == null) {
                return null;
            }

            // `previous()` will return the same element after this call.
            previous = next;

            // Clear the cache.
            next = null;

            ++nextIndex;
            return previous;
        }

        @Override
        public New previous() {
            // Is the element cached?
            if (previous == null) {
                previous = retrievePrevious();
            }

            // Is there a *previous* element?
            if (previous == null) {
                return null;
            }

            // `next()` will return the same element after this call.
            next = previous;

            // Clear the cache.
            previous = null;

            --nextIndex;
            return next;
        }

        /**
         * Searches {@link backingIterator} for the next unfiltered element and returns its mapping.
         */
        private New retrieveNext() {
            while (backingIterator.hasNext()) {
                Optional<New> element = filterMap.apply(backingIterator.next());
                if (element.isPresent()) {
                    return element.get();
                }
            }

            return null;
        }

        /**
         * Searches {@link backingIterator} for the previous unfiltered element and returns its
         * mapping.
         */
        private New retrievePrevious() {
            while (backingIterator.hasPrevious()) {
                Optional<New> element = filterMap.apply(backingIterator.previous());
                if (element.isPresent()) {
                    return element.get();
                }
            }

            return null;
        }

        @Override
        public void add(New element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(New element) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ListIterator<New> listIterator(int index) {
        return new FilterMapListIterator(backingList.listIterator(), index);
    }

    @Override
    public int size() {
        var iterator = listIterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        return iterator.nextIndex();
    }

    // Overridden because the default implementation (at least in OpenJDK) has to traverse backing
    // list one complete time (it's returning `size() == 0`).
    @Override
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    // Overridden because the default implementation (at least in OpenJDK) has to traverse the
    // backing list up to list three time (it's calling `listIterator(size())`). Unfortunately this
    // function always has to traverse the backing list at least once because it can't start at the
    // end of the list (it would have to know `size`).
    @Override
    public int lastIndexOf(Object needle) {
        var iterator = listIterator();
        var lastIndex = -1;

        while (iterator.hasNext()) {
            var index = iterator.nextIndex();
            var element = iterator.next();
            if (needle == null ? element == null : needle.equals(element)) {
                lastIndex = index;
            }
        }

        return lastIndex;
    }
}
