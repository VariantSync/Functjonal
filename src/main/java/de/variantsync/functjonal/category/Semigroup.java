package de.variantsync.functjonal.category;

/**
 * A binary operator over values T.
 *
 * @param <T> Type that forms a semigroup.
 */
@FunctionalInterface
public interface Semigroup<T> {
    /**
     * Composes the two values a and b and returns the result.
     */
    T append(T a, T b);

    /**
     * Asserts that any two values to combine are in fact equal and thus always picks the first element.
     * If two values to combine are not equals, throws a SemiGroupCannotAppend exception.
     *
     * @param <U> Type of group.
     * @return A semigroup which can only append equal elements and
     * throws an error when invoked with two different elements.
     */
    static <U> Semigroup<U> assertEquals() {
        return (a, b) -> {
            if (!a.equals(b)) {
                throw new SemigroupCannotAppend("Assertion failed. " +
                        "The following objects where assumed to be equal but are not: \"" + a + "\"; \"" + b + "\"!");
            }
            return a;
        };
    }
}
