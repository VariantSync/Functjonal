package vevos.functjonal.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListDecorator<T> implements List<T> {
    protected final List<T> wrappee;

    public ListDecorator(final List<T> list) {
        if (list == null) {
            throw new IllegalArgumentException("Given list cannot be null!");
        }
        this.wrappee = list;
    }

    /**
     * @return The list wrapped in this decorator.
     */
    public List<T> unwrap() {
        if (wrappee instanceof ListDecorator<T> l) {
            return l.unwrap();
        }
        return wrappee;
    }

    @Override
    public int size() {
        return wrappee.size();
    }

    @Override
    public boolean isEmpty() {
        return wrappee.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return wrappee.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return wrappee.iterator();
    }

    @Override
    public Object[] toArray() {
        return wrappee.toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1 [] a) {
        return wrappee.toArray(a);
    }

    @Override
    public boolean add(final T t) {
        return wrappee.add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return wrappee.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return wrappee.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return wrappee.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        return wrappee.addAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return wrappee.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return wrappee.retainAll(c);
    }

    @Override
    public void clear() {
        wrappee.clear();
    }

    @Override
    public T get(final int index) {
        return wrappee.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        return wrappee.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        wrappee.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return wrappee.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return wrappee.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return wrappee.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return wrappee.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return wrappee.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return wrappee.subList(fromIndex, toIndex);
    }
}
