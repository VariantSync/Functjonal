package de.variantsync.functjonal.category;

/**
 * A semigroup that does not compose two values to a new value but instead
 * modifies the first value by appending the second value to it.
 * Therefore, it alters the state of the first value passed to it.
 * An in-place semigroup is thus not pure functional and should be used with caution.
 * Every in-place semigroup is also a semigroup in the sense that the result of a composition
 * is the first value to which the second value was appended in place.
 *
 * @param <T> The type that forms a semigroup.
 */
@FunctionalInterface
public interface InplaceSemigroup<T> extends Semigroup<T> {
    void appendToFirst(T a, T b);

    default T append(final T a, final T b) {
        appendToFirst(a, b);
        return a;
    }
}
