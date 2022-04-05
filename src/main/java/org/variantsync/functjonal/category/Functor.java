package org.variantsync.functjonal.category;

import java.util.function.Function;

/**
 * Interface for functors.
 * Functors represent embellished values on which can be operated using map.
 * @param <F> The type of the functor implementing this interface.
 *            F itself should be a generic accepting values of type A (e.g., List<A>).
 *            As this cannot be explicitly modelled in java (it does not support higher-kinded types),
 *            pass F as a raw type here (e.g., List instead of List<A>).
 * @param <A> The value type of the functor.
 */
public interface Functor<F, A> {
    /**
     * Applies the given function f to the values embellished in this functor.
     * Note to developers: Fix the return type to F<B> (i.e., instead of returning List, map should return List<B>).
     * @param f The function to run on the values in this functor.
     * @return Returns a new functor containing the result of f applied to the values in this functor.
     */
    <B> F map(Function<? super A, ? extends B> f);
}
